"""
知识库模块测试脚本

测试知识库的核心功能：
  - 文档处理
  - 文本分块
  - 向量化
  - 知识检索
"""

import asyncio
import sys
from pathlib import Path

sys.path.append(str(Path(__file__).parent.parent))

from app.core.knowledge import (
    DocumentProcessor,
    TextChunker,
    VectorStore,
    KnowledgeRetriever
)
from app.utils.logger import log


async def test_document_processing():
    """测试文档处理"""
    log.info("=== 测试文档处理 ===")

    processor = DocumentProcessor()

    test_file = "data/documents/triage_knowledge.md"
    if Path(test_file).exists():
        try:
            document = await processor.process_file(test_file)
            log.info(f"文档处理成功: {document['id']}")
            log.info(f"文档长度: {len(document['text'])}")
            log.info(f"元数据: {document['metadata']}")
            return document
        except Exception as e:
            log.error(f"文档处理失败: {str(e)}")
            return None
    else:
        log.warning(f"测试文件不存在: {test_file}")
        return None


async def test_text_chunking(document):
    """测试文本分块"""
    log.info("=== 测试文本分块 ===")

    if not document:
        log.warning("没有可用的文档进行分块测试")
        return []

    chunker = TextChunker()

    try:
        chunks = await chunker.chunk_text(
            document["text"],
            document["metadata"]
        )

        log.info(f"分块成功，共 {len(chunks)} 个分块")
        for i, chunk in enumerate(chunks[:3]):
            log.info(f"分块 {i+1}: 长度={len(chunk['text'])}, 内容={chunk['text'][:50]}...")

        return chunks
    except Exception as e:
        log.error(f"文本分块失败: {str(e)}")
        return []


async def test_vector_store(chunks, namespace="test"):
    """测试向量存储"""
    log.info("=== 测试向量存储 ===")

    if not chunks:
        log.warning("没有可用的分块进行向量存储测试")
        return False

    vector_store = VectorStore()

    try:
        result = await vector_store.add_documents(namespace, chunks)
        log.info(f"向量存储结果: 成功={result['success']}, 失败={result['failed']}")
        return result['success'] > 0
    except Exception as e:
        log.error(f"向量存储失败: {str(e)}")
        return False


async def test_knowledge_retrieval(namespace="test"):
    """测试知识检索"""
    log.info("=== 测试知识检索 ===")

    retriever = KnowledgeRetriever()

    test_queries = [
        "胸痛应该挂什么科",
        "头痛头晕怎么办",
        "腹痛恶心是什么病"
    ]

    for query in test_queries:
        try:
            results = await retriever.retrieve(
                namespace=namespace,
                query=query,
                top_k=3,
                min_score=0.0
            )

            log.info(f"查询: {query}")
            log.info(f"找到 {len(results)} 条结果")

            for i, result in enumerate(results):
                score = result.get("score", 0)
                content = result.get("text", "")[:100]
                log.info(f"  结果 {i+1}: 相似度={score:.3f}, 内容={content}...")

        except Exception as e:
            log.error(f"知识检索失败: {query}, 错误: {str(e)}")


async def test_namespace_management():
    """测试命名空间管理"""
    log.info("=== 测试命名空间管理 ===")

    vector_store = VectorStore()

    try:
        namespaces = await vector_store.list_namespaces()
        log.info(f"可用命名空间: {namespaces}")

        for namespace in namespaces:
            stats = await vector_store.get_namespace_stats(namespace)
            log.info(f"命名空间 {namespace}: {stats}")

    except Exception as e:
        log.error(f"命名空间管理测试失败: {str(e)}")


async def main():
    """主测试函数"""
    log.info("开始知识库模块测试")

    try:
        document = await test_document_processing()
        chunks = await test_text_chunking(document)
        success = await test_vector_store(chunks, "test_triage")

        if success:
            await test_knowledge_retrieval("test_triage")

        await test_namespace_management()

        log.info("知识库模块测试完成")

    except Exception as e:
        log.error(f"测试过程中出现错误: {str(e)}")


if __name__ == "__main__":
    asyncio.run(main())
