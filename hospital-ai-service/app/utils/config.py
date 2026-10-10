from pydantic_settings import BaseSettings
from typing import List, Optional
import os


class Settings(BaseSettings):
    """应用配置"""

    # 应用配置
    APP_NAME: str = "hospital-ai-service"
    APP_VERSION: str = "1.0.0"
    APP_ENV: str = "development"
    DEBUG: bool = True
    LOG_LEVEL: str = "INFO"

    # 服务器配置
    HOST: str = "0.0.0.0"
    PORT: int = 8000
    WORKERS: int = 4

    # 通义千问配置
    DASH_SCOPE_API_KEY: Optional[str] = None
    QWEN_MODEL: str = "qwen-plus"
    QWEN_TEMPERATURE: float = 0.7
    QWEN_MAX_TOKENS: int = 2000
    DASHSCOPE_BASE_URL: str = "https://dashscope.aliyuncs.com/compatible-mode/v1"

    # Pinecone 配置
    PINECONE_API_KEY: Optional[str] = None
    PINECONE_ENVIRONMENT: str = "us-east-1"
    PINECONE_INDEX: str = "hospital-ai-index"
    PINECONE_DIMENSION: int = 1536

    # MongoDB 配置
    MONGODB_URI: str = "mongodb://root:${MONGODB_PASSWORD}@127.0.0.1:27017/?authSource=admin"
    MONGODB_DATABASE: str = "hospital_ai"

    # Redis 配置
    REDIS_HOST: str = "127.0.0.1"
    REDIS_PORT: int = 6379
    REDIS_DB: int = 0
    REDIS_PASSWORD: Optional[str] = None

    # RabbitMQ 配置
    RABBITMQ_HOST: str = "127.0.0.1"
    RABBITMQ_PORT: int = 5672
    RABBITMQ_USER: str = "guest"
    RABBITMQ_PASSWORD: str = "guest"
    RABBITMQ_VHOST: str = "/"

    # 医院系统配置
    HOSPITAL_API_BASE_URL: str = "http://localhost:8071"
    HOSPITAL_CUSTOMER_BASE_URL: str = "http://localhost:8061"
    HOSPITAL_SYSTEM_BASE_URL: str = "http://localhost:9201"
    HOSPITAL_API_TIMEOUT: int = 30
    HOSPITAL_API_TOKEN: str = ""

    # 事件驱动配置
    EVENT_RETENTION_DAYS: int = 90
    REMINDER_DEFAULT_DELAY_SECONDS: int = 7200  # 默认提醒延迟 2 小时
    FOLLOW_UP_REMINDER_DELAY_SECONDS: int = 86400  # 检查结果后的复诊提醒，默认 24 小时
    MEDICATION_REMINDER_INTERVAL_HOURS: int = 8  # 用药提醒间隔
    MEDICATION_REMINDER_INTERVAL_SECONDS: Optional[int] = None  # 测试时可覆盖小时配置
    MEDICATION_REMINDER_TIMES: int = 21
    PHARMACY_PICKUP_LOCATION: str = "门诊一楼药房"

    # WebSocket 配置
    WS_HEARTBEAT_INTERVAL: int = 30
    WS_MAX_CONNECTIONS: int = 1000

    # SSE 配置
    SSE_HEARTBEAT_INTERVAL: int = 15
    SSE_TIMEOUT: int = 300

    # 监控配置
    PROMETHEUS_PORT: int = 9090
    ENABLE_METRICS: bool = True

    # 安全配置
    SECRET_KEY: str = "your-secret-key-change-this-in-production"
    ALLOWED_ORIGINS: str = "http://localhost:3000,http://localhost:8080"

    @property
    def allowed_origins_list(self) -> List[str]:
        return [origin.strip() for origin in self.ALLOWED_ORIGINS.split(",")]

    def validate_api_keys(self):
        """验证必需的 API 密钥是否已配置"""
        if not self.DASH_SCOPE_API_KEY:
            raise ValueError(
                "DASH_SCOPE_API_KEY 未配置！请通过系统环境变量设置：\n"
                "Windows: set DASH_SCOPE_API_KEY=your-api-key\n"
                "Linux/Mac: export DASH_SCOPE_API_KEY=your-api-key"
            )

        if not self.PINECONE_API_KEY:
            raise ValueError(
                "PINECONE_API_KEY 未配置！请通过系统环境变量设置：\n"
                "Windows: set PINECONE_API_KEY=your-api-key\n"
                "Linux/Mac: export PINECONE_API_KEY=your-api-key"
            )

    class Config:
        env_file = ".env"
        case_sensitive = True


settings = Settings()
settings.validate_api_keys()
