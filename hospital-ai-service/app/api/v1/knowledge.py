"""
知识库管理API - 提供知识库管理的HTTP接口

提供完整的知识库管理功能：
  - 文档上传和处理
  - 知识库查询和搜索
  - 知识库维护
  - 统计信息
"""

from fastapi import APIRouter, UploadFile, File, HTTPException, Query
from typing import List, Optional, Dict, Any
from pydantic import BaseModel, Field
from datetime import datetime

from app.core.knowledge import (
    VectorStore,
    DocumentProcessor,
    TextChunker,
    KnowledgeRetriever
)
from app.utils.logger import log

router = APIRouter(prefix="/knowledge", tags=["知识库管理"])


class DocumentUploadResponse(BaseModel):
    """文档上传响应"""
    success: bool
    message: str
    document_id: Optional[str] = None
    chunks_created: int = 0


class KnowledgeQueryRequest(BaseModel):
    """知识查询请求"""
    namespace: str = Field(..., description="知识库命名空间")
    query: str = Field(..., description="查询文本")
    top_k: int = Field(5, description="返回结果数量")
    min_score: float = Field(0.6, description="最小相似度分数")


class KnowledgeQueryResponse(BaseModel):
    """知识查询响应"""
    query: str
    namespace: str
    results: List[Dict[str, Any]]
    total_results: int
    context: Optional[str] = None


class StatsResponse(BaseModel):
    """统计信息响应"""
    namespace: str
    available: bool
    document_count: int = 0
    last_updated: Optional[str] = None


class NamespaceListResponse(BaseModel):
    """命名空间列表响应"""
    namespaces: List[str]
    total: int


@router.post("/upload", response_model=DocumentUploadResponse)
async def upload_document(
    file: UploadFile = File(...),
    namespace: str = Query(..., description="目标知识库命名空间"),
    chunk_size: int = Query(500, description="分块大小"),
    chunk_overlap: int = Query(50, description="分块重叠大小")
):
    """
    上传文档到知识库

    支持的格式: .txt, .md, .json, .csv
    """
    try:
        if not file.filename:
            raise HTTPException(status_code=400, detail="文件名不能为空")

        log.info(f"开始处理文档上传 - 文件: {file.filename}, 命名空间: {namespace}")

        temp_file_path = f"/tmp/{file.filename}"
        with open(temp_file_path, "wb") as buffer:
            content = await file.read()
            buffer.write(content)

        document_processor = DocumentProcessor()
        chunker = TextChunker()
        chunker.update_config(chunk_size=chunk_size, chunk_overlap=chunk_overlap)

        document = await document_processor.process_file(
            temp_file_path,
            metadata={"namespace": namespace, "source": "upload"}
        )

        chunks = await chunker.chunk_text(
            document["text"],
            document["metadata"]
        )

        vector_store = VectorStore()
        result = await vector_store.add_documents(namespace, chunks)

        import os
        os.remove(temp_file_path)

        if result["success"] > 0:
            return DocumentUploadResponse(
                success=True,
                message=f"文档上传成功，创建了 {result['success']} 个知识块",
                document_id=document["id"],
                chunks_created=result["success"]
            )
        else:
            raise HTTPException(
                status_code=500,
                detail=f"文档处理失败: {result.get('message', '未知错误')}"
            )

    except HTTPException:
        raise
    except Exception as e:
        log.error(f"文档上传失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"文档上传失败: {str(e)}")


@router.post("/upload/text", response_model=DocumentUploadResponse)
async def upload_text(
    request: Dict[str, Any],
    namespace: str = Query(..., description="目标知识库命名空间")
):
    """
    上传纯文本到知识库
    """
    try:
        text = request.get("text", "")
        if not text or not text.strip():
            raise HTTPException(status_code=400, detail="文本内容不能为空")

        metadata = request.get("metadata", {})
        chunk_config = request.get("chunk_config", {})

        log.info(f"开始处理文本上传 - 命名空间: {namespace}, 文本长度: {len(text)}")

        document_processor = DocumentProcessor()
        chunker = TextChunker()

        if chunk_config:
            chunker.update_config(**chunk_config)

        document = await document_processor.process_text(
            text,
            metadata={**metadata, "namespace": namespace, "source": "text_input"}
        )

        chunks = await chunker.chunk_text(
            document["text"],
            document["metadata"]
        )

        vector_store = VectorStore()
        result = await vector_store.add_documents(namespace, chunks)

        if result["success"] > 0:
            return DocumentUploadResponse(
                success=True,
                message=f"文本上传成功，创建了 {result['success']} 个知识块",
                document_id=document["id"],
                chunks_created=result["success"]
            )
        else:
            raise HTTPException(
                status_code=500,
                detail=f"文本处理失败: {result.get('message', '未知错误')}"
            )

    except HTTPException:
        raise
    except Exception as e:
        log.error(f"文本上传失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"文本上传失败: {str(e)}")


@router.post("/query", response_model=KnowledgeQueryResponse)
async def query_knowledge(request: KnowledgeQueryRequest):
    """
    查询知识库
    """
    try:
        log.info(f"知识库查询 - 命名空间: {request.namespace}, 查询: {request.query[:50]}...")

        retriever = KnowledgeRetriever()

        result = await retriever.retrieve_with_context(
            namespace=request.namespace,
            query=request.query,
            top_k=request.top_k,
            min_score=request.min_score
        )

        return KnowledgeQueryResponse(
            query=request.query,
            namespace=request.namespace,
            results=result["results"],
            total_results=result.get("result_count", len(result["results"])),
            context=result.get("context")
        )

    except Exception as e:
        log.error(f"知识库查询失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"知识库查询失败: {str(e)}")


@router.get("/namespaces", response_model=NamespaceListResponse)
async def list_namespaces():
    """
    列出所有知识库命名空间
    """
    try:
        vector_store = VectorStore()
        namespaces = await vector_store.list_namespaces()

        return NamespaceListResponse(
            namespaces=namespaces,
            total=len(namespaces)
        )

    except Exception as e:
        log.error(f"获取命名空间列表失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"获取命名空间列表失败: {str(e)}")


@router.get("/stats/{namespace}", response_model=StatsResponse)
async def get_namespace_stats(namespace: str):
    """
    获取知识库统计信息
    """
    try:
        vector_store = VectorStore()
        stats = await vector_store.get_namespace_stats(namespace)

        return StatsResponse(
            namespace=namespace,
            available=stats.get("available", False),
            document_count=stats.get("document_count", 0),
            last_updated=stats.get("last_updated")
        )

    except Exception as e:
        log.error(f"获取统计信息失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"获取统计信息失败: {str(e)}")


@router.delete("/documents/{namespace}")
async def delete_documents(
    namespace: str,
    document_ids: Optional[List[str]] = Query(None, description="要删除的文档ID列表"),
    delete_all: bool = Query(False, description="是否删除所有文档")
):
    """
    删除知识库文档
    """
    try:
        if not document_ids and not delete_all:
            raise HTTPException(
                status_code=400,
                detail="必须提供 document_ids 或设置 delete_all=true"
            )

        vector_store = VectorStore()
        success = await vector_store.delete_documents(
            namespace=namespace,
            document_ids=document_ids,
            delete_all=delete_all
        )

        if success:
            action = "清空所有文档" if delete_all else f"删除 {len(document_ids)} 个文档"
            return {"success": True, "message": f"{action}成功"}
        else:
            raise HTTPException(status_code=500, detail="删除文档失败")

    except HTTPException:
        raise
    except Exception as e:
        log.error(f"删除文档失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"删除文档失败: {str(e)}")


@router.post("/batch/upload")
async def batch_upload_documents(
    files: List[UploadFile] = File(...),
    namespace: str = Query(..., description="目标知识库命名空间"),
    chunk_size: int = Query(500, description="分块大小"),
    chunk_overlap: int = Query(50, description="分块重叠大小")
):
    """
    批量上传文档到知识库
    """
    try:
        if not files:
            raise HTTPException(status_code=400, detail="文件列表不能为空")

        log.info(f"开始批量上传文档 - 文件数: {len(files)}, 命名空间: {namespace}")

        document_processor = DocumentProcessor()
        chunker = TextChunker()
        chunker.update_config(chunk_size=chunk_size, chunk_overlap=chunk_overlap)
        vector_store = VectorStore()

        results = {
            "total": len(files),
            "success": 0,
            "failed": 0,
            "errors": [],
            "total_chunks": 0
        }

        for file in files:
            try:
                temp_file_path = f"/tmp/{file.filename}"
                with open(temp_file_path, "wb") as buffer:
                    content = await file.read()
                    buffer.write(content)

                document = await document_processor.process_file(
                    temp_file_path,
                    metadata={"namespace": namespace, "source": "batch_upload"}
                )

                chunks = await chunker.chunk_text(
                    document["text"],
                    document["metadata"]
                )

                result = await vector_store.add_documents(namespace, chunks)

                import os
                os.remove(temp_file_path)

                if result["success"] > 0:
                    results["success"] += 1
                    results["total_chunks"] += result["success"]
                else:
                    results["failed"] += 1
                    results["errors"].append(f"{file.filename}: {result.get('message', '未知错误')}")

            except Exception as e:
                results["failed"] += 1
                results["errors"].append(f"{file.filename}: {str(e)}")

        log.info(f"批量上传完成 - 成功: {results['success']}, 失败: {results['failed']}")
        return results

    except HTTPException:
        raise
    except Exception as e:
        log.error(f"批量上传失败: {str(e)}")
        raise HTTPException(status_code=500, detail=f"批量上传失败: {str(e)}")
