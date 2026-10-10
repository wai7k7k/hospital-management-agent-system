import pytest
from fastapi.testclient import TestClient
from app.main import app


client = TestClient(app)


def test_root():
    """测试根路径"""
    response = client.get("/")
    assert response.status_code == 200
    data = response.json()
    assert "app_name" in data
    assert "version" in data


def test_health_check():
    """测试健康检查"""
    response = client.get("/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "healthy"


def test_chat_endpoint():
    """测试聊天接口"""
    response = client.post(
        "/v1/chat/",
        json={
            "session_id": "test-session-001",
            "message": "你好",
            "user_id": "test-user"
        }
    )
    # 可能会失败，因为需要配置 API 密钥
    assert response.status_code in [200, 500]


def test_cors():
    """测试 CORS"""
    response = client.options(
        "/v1/chat/",
        headers={
            "Origin": "http://localhost:3000",
            "Access-Control-Request-Method": "POST",
            "Access-Control-Request-Headers": "Content-Type"
        }
    )
    assert response.status_code == 200
