from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import JSONResponse
from contextlib import asynccontextmanager
from app.utils.config import settings
from app.utils.logger import log, setup_logger
from app.api.v1 import chat, websocket, knowledge
from app.core.events.event_router import router as event_router
from prometheus_client import Counter, Histogram, generate_latest
from fastapi.responses import Response
import time
import asyncio
from datetime import datetime


# Prometheus 指标
REQUEST_COUNT = Counter('http_requests_total', 'Total HTTP requests', ['method', 'endpoint', 'status'])
REQUEST_DURATION = Histogram('http_request_duration_seconds', 'HTTP request duration', ['method', 'endpoint'])


@asynccontextmanager
async def lifespan(app: FastAPI):
    from app.utils.instance_lock import single_instance
    with single_instance():
        async with service_lifespan(app):
            yield


@asynccontextmanager
async def service_lifespan(app: FastAPI):
    """应用生命周期管理"""
    log.info("应用启动中...")

    # 初始化工作流
    from app.core.graph.workflow import get_workflow
    get_workflow()

    # 初始化 MongoDB
    from app.utils.cache import MongoDBCache
    mongo_cache = MongoDBCache()
    await mongo_cache.client.admin.command("ping")

    # 初始化 记忆存储（Agent 记忆隔离 + MongoDB 持久化）
    from app.core.memory import get_memory_store
    get_memory_store()
    log.info("Agent 记忆存储已初始化")

    # 初始化 Agent 层（TriageAgent / ReportAgent / MedicationAgent / HospitalAgent）
    from app.core.agents import TriageAgent, ReportAgent, MedicationAgent, HospitalAgent
    log.info("Agent 层已就绪 — TriageAgent, ReportAgent, MedicationAgent, HospitalAgent")

    # 初始化 Pinecone
    from app.integrations.vector.pinecone_client import PineconeClient
    pinecone_client = PineconeClient()

    # 初始化提醒调度服务
    from app.core.services.reminder_service import get_reminder_service
    get_reminder_service()

    # 启动 RabbitMQ 事件消费者（后台线程）
    from app.integrations.mq.consumer import get_consumer_manager, shutdown_consumer_manager
    consumer_manager = get_consumer_manager()
    try:
        consumer_manager.start_all()
        log.info("RabbitMQ 事件消费者已启动")
    except Exception as e:
        log.warning(f"RabbitMQ 消费者启动失败（非致命）: {str(e)}")

    log.info("应用启动完成")

    yield

    # 清理资源
    log.info("应用关闭中...")
    from app.core.services.reminder_service import get_reminder_service
    get_reminder_service().cancel_all()
    await asyncio.to_thread(shutdown_consumer_manager)
    await mongo_cache.close()
    log.info("应用已关闭")


# 创建 FastAPI 应用
app = FastAPI(
    title=settings.APP_NAME,
    version=settings.APP_VERSION,
    description="医院智能 AI 服务 - 基于 LangGraph 的医疗助手",
    lifespan=lifespan
)

# CORS 配置
app.add_middleware(
    CORSMiddleware,
    allow_origins=settings.allowed_origins_list,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


# 中间件：请求日志
@app.middleware("http")
async def log_requests(request, call_next):
    """记录请求日志"""
    start_time = time.time()

    log.info(f"收到请求 - {request.method} {request.url.path}")

    response = await call_next(request)

    duration = time.time() - start_time

    # Prometheus 指标
    REQUEST_COUNT.labels(
        method=request.method,
        endpoint=request.url.path,
        status=response.status_code
    ).inc()

    REQUEST_DURATION.labels(
        method=request.method,
        endpoint=request.url.path
    ).observe(duration)

    log.info(f"请求完成 - {request.method} {request.url.path} - {response.status_code} - {duration:.3f}s")

    return response


# 注册路由
app.include_router(chat.router)
app.include_router(websocket.router)
app.include_router(knowledge.router)
app.include_router(event_router)


# 健康检查
@app.get("/health")
async def health_check():
    """健康检查"""
    return {
        "status": "healthy",
        "app_name": settings.APP_NAME,
        "version": settings.APP_VERSION,
        "timestamp": str(datetime.now())
    }


# Prometheus 指标端点
@app.get("/metrics")
async def metrics():
    """Prometheus 指标"""
    return Response(generate_latest(), media_type="text/plain")


# 根路径
@app.get("/")
async def root():
    """根路径"""
    return {
        "app_name": settings.APP_NAME,
        "version": settings.APP_VERSION,
        "description": "医院智能 AI 服务",
        "docs": "/docs",
        "health": "/health"
    }


# 异常处理
@app.exception_handler(Exception)
async def global_exception_handler(request, exc):
    """全局异常处理"""
    log.error(f"未处理的异常: {str(exc)}")

    return JSONResponse(
        status_code=500,
        content={
            "code": 500,
            "message": "内部服务器错误",
            "detail": str(exc) if settings.DEBUG else "请联系管理员"
        }
    )


if __name__ == "__main__":
    import uvicorn

    setup_logger()

    uvicorn.run(
        "app.main:app",
        host=settings.HOST,
        port=settings.PORT,
        workers=settings.WORKERS,
        reload=settings.DEBUG,
        log_level=settings.LOG_LEVEL.lower()
    )
