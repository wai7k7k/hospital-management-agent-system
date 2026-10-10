---
name: bug-20260608-ai-404-and-duplicate-msg
description: 前端调用 AI 服务 404 + 小智聊天发送一句显示两句
metadata:
  type: reference
---

# BUG-20260608-01: 前端 AI 服务 404 + 消息重复显示

| 字段 | 内容 |
|------|------|
| **编号** | BUG-20260608-01 |
| **发现日期** | 2026-06-08 |
| **修复日期** | 2026-06-08 |
| **严重程度** | 🔴严重 |
| **影响模块** | hospital-ai-service, hospital-management-system-ui, nginx |
| **修复人** | xinyaohe |

---

## 一、问题描述

1. 前端点击预约挂号，"小智"聊天功能报错：`POST /api/ai/v1/chat/stream` 返回 404
2. 发送一句话后，聊天界面显示两条一模一样的用户消息，AI 也回复两次

## 二、根因分析

### Bug 1: 404 Not Found

根源在**两处路由缺失**：

- **Nginx**（`docker/nginx/conf/nginx.conf`）：缺少 `/api/ai/` 的 location 代理规则。AI 服务在 port 8000，路由前缀是 `/v1/chat/stream`，但 Nginx 只配置了 `/prod-api/` → gateway:8080 和 `/` → 静态文件。请求落到 `/` 被当静态文件处理，返回 404。

- **前端 devServer**（`vue.config.js`）：缺少 `/api/ai` 的 proxy 规则。前端开发时走 webpack-dev-server，不走 Nginx，devServer 也不认识这个路径。

两处都得修，否则 Docker 部署和本地开发至少有一个会挂。

### Bug 2: 消息重复显示

根因在前端 `src/store/modules/ai.js` 的 **SSE 降级链路重复添加用户消息**：

```
sendStreamMessage()
  → ADD_MESSAGE(userMsg)     ← 加了一次用户消息 (第75行)
  → SSE 失败，触发 onError
    → _fallbackHttp()
      → ADD_MESSAGE(userMsg) ← 又加了一次用户消息 (第143行)
```

两个调用路径各自加了一次，导致界面上出现两条一样的用户消息。

### 定位过程

1. F12 看到 `SSE stream failed, falling back to HTTP: HTTP 404`，确认 SSE 不通
2. 检查 AI 服务：`curl localhost:8000/health` 返回 200，服务正常
3. 检查 Nginx 配置：发现没有 `/api/ai/` 路由
4. 检查 `vue.config.js`：发现 devServer proxy 也没有 `/api/ai`
5. 审查 `ai.js` store：发现 `sendStreamMessage` 和 `_fallbackHttp` 都调了 `ADD_MESSAGE(userMsg)`，SSE 失败降级时形成重复添加

## 三、修复方案

### 涉及文件

| 文件 | 修改内容 | 原因 |
|------|----------|------|
| `docker/nginx/conf/nginx.conf` | 新增 `/api/ai/` location，proxy_pass 到 127.0.0.1:8000 | Docker 环境 Nginx 路由缺失 |
| `vue.config.js` | 新增 `/api/ai` proxy 规则，target: localhost:8000 | 本地开发 devServer 路由缺失 |
| `src/store/modules/ai.js` | `_fallbackHttp` 加 `skipUserMsg` 参数；`onError` 调用时传 `{ skipUserMsg: true }` | 修复降级链路重复添加用户消息 |

### 方案对比

| 方案 | 优点 | 缺点 | 结论 |
|------|------|------|------|
| Nginx + devServer 双修（当前） | Docker 部署和本地开发都能用 | 改两个地方 | ✅ 采纳 |
| 只修 Nginx，不改 devServer | 少改一个文件 | 本地开发仍 404 | ❌ |
| 改 AI 服务路由前缀为 `/api/ai/v1/...` | 不改 Nginx | 破坏 RESTful 惯例，旧客户端不兼容 | ❌ |

## 四、验证方法

- [ ] 本地 `npm run dev` 启动前端，打开小智聊天，发送消息，确认不报 404
- [ ] 确认消息不重复：发送一句，界面只显示一句
- [ ] SSE 流式正常：回复逐字出现（而非一次性返回）
- [ ] Docker 环境：`docker-compose up nginx` 后测试同样场景

## 五、学习收获

### 技术知识点

* **webpack-dev-server proxy 的 pathRewrite**：`{ '^/api/ai': '' }` 会把 `/api/ai/v1/chat/stream` 重写为 `/v1/chat/stream` 再转发给 target
* **Nginx proxy_pass 尾部斜杠**：带 `/` 会自动剥离匹配的 location 前缀，不带则保留原路径
* **SSE 降级陷阱**：流式失败降级到 HTTP 时，要小心调用链里是否已有副作用（如已添加用户消息），否则出现重复数据

### 经验教训

* 排查 404 时要自底向上：服务本身 → Nginx/Proxy → 前端请求，不要只看一层
* 前端乐观更新 + 降级逻辑的组合容易产生重复数据，写降级代码时先检查上游做了什么

### 关联知识

* [[project-knowledge/modules/hospital-ai-service]]
* [[project-knowledge/modules/ruoyi-gateway]]
* [[architecture/ADR-20260608-01-nginx-ai-route]]
