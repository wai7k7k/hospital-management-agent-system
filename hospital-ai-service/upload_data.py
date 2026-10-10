"""
知识库数据上传脚本 - 配置后直接运行即可

使用方法:
    1. 在下面配置好要上传的目录或文件
    2. 直接运行: python upload_data.py
"""

import asyncio
import sys
from pathlib import Path
from dotenv import load_dotenv

sys.path.insert(0, str(Path(__file__).parent))

# 加载 .env 文件
load_dotenv()

from app.core.knowledge.document import DocumentProcessor
from app.core.knowledge.chunker import TextChunker
from app.core.knowledge.vector_store import VectorStore


# ==================== 配置区域 ====================
# 在这里配置要上传的目录或文件和对应的命名空间

UPLOAD_CONFIG = [
    {
        "path": "D:/ai_langchain4j/java-ai-langchain4j/src/main/resources/knowledge/triage-knowledge.json",           # 分诊知识文件
        "namespace": "triage",
        "chunk_size": 500,
        "chunk_overlap": 50,
    },
    {
        "path": "D:/ai_langchain4j/java-ai-langchain4j/src/main/resources/knowledge/medication-knowledge.json",       # 用药知识文件
        "namespace": "medication",
        "chunk_size": 600,
        "chunk_overlap": 80,
    },
    {
        "path": "D:/ai_langchain4j/java-ai-langchain4j/src/main/resources/knowledge/hospital-output",              # 医院信息目录
        "namespace": "hospital",
        "chunk_size": 500,
        "chunk_overlap": 50,
    },
    {
        "path": "D:/ai_langchain4j/java-ai-langchain4j/src/main/resources/knowledge/report-knowledge.json",               # 报告解读文件
        "namespace": "report",
        "chunk_size": 700,
        "chunk_overlap": 100,
    },
]

# ==================== 配置结束 ====================


async def upload_path(config: dict):
    """
    上传文件或目录到知识库

    Args:
        config: 配置字典，包含 path, namespace, chunk_size, chunk_overlap
    """
    path_str = config["path"]
    namespace = config["namespace"]
    chunk_size = config.get("chunk_size", 500)
    chunk_overlap = config.get("chunk_overlap", 50)

    print(f"\n{'='*70}")
    print(f"开始上传: {path_str}")
    print(f"命名空间: {namespace}")
    print(f"{'='*70}\n")

    try:
        path = Path(path_str)

        if not path.exists():
            print(f"✗ 跳过: 路径不存在 - {path_str}")
            return False

        documents = []

        if path.is_file():
            print("步骤 1/3: 处理文件...")
            dp = DocumentProcessor()
            doc = await dp.process_file(path_str)
            documents = [doc]
            print(f"✓ 文件处理成功: {doc['metadata']['filename']} ({doc['metadata']['file_size']} bytes)")

        elif path.is_dir():
            print("步骤 1/3: 扫描并处理文档...")
            dp = DocumentProcessor()
            documents = await dp.process_directory(path_str)
            print(f"✓ 找到并处理 {len(documents)} 个文档")

            if documents:
                print("\n处理的文件:")
                for doc in documents:
                    print(f"  - {doc['metadata']['filename']} ({doc['metadata']['file_size']} bytes)")
        else:
            print(f"✗ 跳过: 不支持的路径类型 - {path_str}")
            return False

        if not documents:
            print("✗ 跳过: 没有找到支持的文件")
            return False

        # 2. 批量分块
        print(f"\n步骤 2/3: 批量分块 (chunk_size={chunk_size}, overlap={chunk_overlap})...")
        chunker = TextChunker()
        all_chunks = await chunker.chunk_documents(documents)
        print(f"✓ 分块完成，共生成 {len(all_chunks)} 个知识块")

        # 3. 存储到向量库
        print("\n步骤 3/3: 存储到向量库...")
        vs = VectorStore()
        result = await vs.add_documents(namespace, all_chunks)

        if result['success'] > 0:
            print(f"✓ 上传成功!")
            print(f"  - 成功: {result['success']} 个知识块")
            if result['failed'] > 0:
                print(f"  - 失败: {result['failed']} 个知识块")
                if result['errors']:
                    print(f"  - 错误详情:")
                    for error in result['errors']:
                        print(f"    * {error}")
        else:
            print(f"✗ 上传失败: {result.get('message', '未知错误')}")
            return False

        print(f"\n{'='*70}")
        print(f"✓ 上传完成: {path_str}")
        print(f"{'='*70}\n")
        return True

    except Exception as e:
        print(f"✗ 上传失败: {str(e)}")
        import traceback
        traceback.print_exc()
        return False


async def main():
    """
    主函数：依次上传所有配置的路径
    """
    print("\n" + "="*70)
    print("知识库数据上传工具")
    print("="*70)
    print(f"\n共配置 {len(UPLOAD_CONFIG)} 个路径需要上传\n")

    results = {
        "total": len(UPLOAD_CONFIG),
        "success": 0,
        "failed": 0,
        "details": []
    }

    for i, config in enumerate(UPLOAD_CONFIG, 1):
        print(f"\n[{i}/{len(UPLOAD_CONFIG)}] 处理配置...")

        success = await upload_path(config)

        results["details"].append({
            "path": config["path"],
            "namespace": config["namespace"],
            "success": success
        })

        if success:
            results["success"] += 1
        else:
            results["failed"] += 1

    # 打印汇总结果
    print("\n" + "="*70)
    print("上传汇总")
    print("="*70)
    print(f"总计: {results['total']} 个路径")
    print(f"成功: {results['success']} 个")
    print(f"失败: {results['failed']} 个")

    if results['failed'] > 0:
        print("\n失败的路径:")
        for detail in results['details']:
            if not detail['success']:
                print(f"  ✗ {detail['path']} (namespace: {detail['namespace']})")

    print("\n" + "="*70 + "\n")

    return results['failed'] == 0


if __name__ == '__main__':
    success = asyncio.run(main())
    sys.exit(0 if success else 1)
