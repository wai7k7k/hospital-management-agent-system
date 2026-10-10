---
name: adr-20260608-nginx-ai-route
description: Nginx 新增 /api/ai/ 路由，将 AI 服务请求代理到 hospital-ai-service:8000
metadata:
  type: reference
---

# ADR-20260608-01: Nginx 新增 AI 服务路由

| 字段 | 内容 |
|------|------|
| **编号** | ADR-20260608-01 |
| **日期** | 2026-06-08 |
| **状态** | ✅ 已采纳 |
| **决策者** | xinyaohe |

---

## 背景

前端调用 `/api/ai/v1/chat/stream` 返回 404。原因是 Nginx 缺少 `/api/ai/` 路由规则，请求落入 `location /` 被当作静态文件处理。AI 服务运行在 port 8000，路由前缀为 `/v1/chat/stream`。

## 决策内容

在 `docker/nginx/conf/nginx.conf` 中新增 location 块：

```nginx
location /api/ai/ {
    proxy_pass http://127.0.0.1:8000/;
    proxy_buffering off;
    proxy_cache off;
    proxy_read_timeout 300s;
}
```

## 考虑的备选方案

| 方案 | 优点 | 缺点 | 结论 |
|------|------|------|------|
| Nginx 新增 `/api/ai/` 代理 | 不改前端、不改 AI 服务、天然支持 SSE | 需要在 Nginx 层面维护 | ✅ 采纳 |
| 修改 AI 服务路由前缀为 `/api/ai/v1/chat/stream` | 不依赖 Nginx | 需要重启 AI 服务、破坏 RESTful 惯例 | ❌ 否决 |
| 经 Gateway 路由 | 统一网关管理 | Gateway 对 SSE 支持差、需要 Nacos 配路由 | ❌ 否决 |

## 影响范围

* **Nginx 配置**: 新增 1 个 location 块
* **AI 服务**: 无需改动
* **前端**: 无需改动
* **Gateway**: 不受影响（请求不再经过 Gateway）

## 后果

### 正面

* SSE 流正常工作，缓存关闭保证实时推送
* 300s 超时覆盖长连接场景
* Nginx 自动剥离 `/api/ai/` 前缀

### 负面

* 多了一条 Nginx 规则需维护
* AI 服务不经过 Gateway 鉴权链路（当前可接受，后续如需鉴权再补充）
