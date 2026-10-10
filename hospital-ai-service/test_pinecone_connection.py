import os
from pinecone import Pinecone

api_key = os.environ.get("PINECONE_API_KEY")
index_name = os.environ.get("PINECONE_INDEX", "xiaozhi-index")

print(f"PINECONE_API_KEY 存在: {bool(api_key)}")
print(f"PINECONE_INDEX = {index_name}")

if not api_key:
    print("错误：未找到 PINECONE_API_KEY，请检查环境变量或 .env 文件")

try:
    pc = Pinecone(api_key=api_key)
    print("✓ Pinecone 客户端初始化成功")

    # 列出所有索引
    indexes = pc.list_indexes()
    print(f"索引列表: {[idx.name for idx in indexes]}")

    # 尝试连接你的索引
    if index_name in [idx.name for idx in indexes]:
        index = pc.Index(index_name)
        stats = index.describe_index_stats()
        print(f"✓ 成功连接到索引 '{index_name}'")
        print(f"  总向量数: {stats.total_vector_count}")
        print(f"  已有命名空间: {list(stats.namespaces.keys())}")
    else:
        print(f"✗ 索引 '{index_name}' 不存在于你的 Pinecone 账户中")

except Exception as e:
    print(f"✗ 连接失败: {type(e).__name__} - {e}")
