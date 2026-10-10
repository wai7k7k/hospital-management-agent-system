# 医院管理系统 (Hospital Management System)

## 项目概述

基于 **RuoYi-Cloud-Plus** 微服务框架的医院管理系统，集成 AI 智能诊断服务。

- **框架**: Spring Cloud 2021.0.3 + Spring Boot 2.7.2
- **语言**: Java 1.8 + Python 3.10+
- **注册中心**: Nacos
- **网关**: Spring Cloud Gateway (port 8080)
- **前端**: Vue (独立仓库 [hospital-management-system-ui](https://gitee.com/ataoxz/hospital-management-system-ui))

## 模块结构

| 模块 | 端口 | 技术栈 | 职责 |
|------|------|--------|------|
| `ruoyi-gateway` | 8080 | Spring Cloud Gateway | API 网关 + 鉴权 |
| `ruoyi-auth` | 9200 | Sa-Token | 认证服务 |
| `ruoyi-modules/ruoyi-system` | 9201 | Spring Boot | 系统管理 |
| `ruoyi-modules/ruoyi-gen` | 9202 | Velocity | 代码生成 |
| `ruoyi-modules/ruoyi-job` | 9203 | XXL-Job | 定时任务 |
| `ruoyi-modules/ruoyi-resource` | 9300 | Minio | 资源/OSS |
| `ruoyi-modules/ylxm-customer` | 8061 | Dubbo + MyBatis-Plus | 患者/客户管理 |
| `ruoyi-modules/ylxm-reghos` | 8071 | Dubbo + RabbitMQ | 挂号/HIS 核心 |
| `hospital-ai-service` | 8000 | FastAPI + LangGraph | AI 智能诊断 |
| `ruoyi-visual/ruoyi-monitor` | 9100 | Spring Boot Admin | 监控中心 |

## 关键配置文件

| 文件 | 作用 |
|------|------|
| `docker/nginx/conf/nginx.conf` | Nginx 反向代理规则 |
| `docker/docker-compose.yml` | 基础设施容器编排 |
| `hospital-ai-service/.env` | AI 服务配置 |
| `ruoyi-gateway/src/main/resources/bootstrap.yml` | 网关 Nacos 配置 |

## 外部依赖

| 服务 | 地址 | 用途 |
|------|------|------|
| Nacos | localhost:8848 | 注册中心 + 配置中心 |
| Redis | 127.0.0.1:6379 | 缓存 |
| MySQL | 127.0.0.1:3306 | 业务数据库 |
| MongoDB | 127.0.0.1:27017 | AI 对话记忆 |
| RabbitMQ | 127.0.0.1:5672 | 事件消息 |
| Pinecone | cloud | 向量知识库 |
| 通义千问 | API | LLM 推理 |

## 工作流规则

### 代码修改前强制自检（调用 Edit/Write 之前必须全部通过）

**以下 5 项，每一项都必须在对话中输出过可见文字，缺一项就不能动手改代码：**

| # | 检查项 | 要求 |
|---|--------|------|
| 1 | 问题分析 | 描述了现象、涉及模块、影响范围 |
| 2 | 定位过程 | 说了排查路径、中间发现、最终定位到的根因 |
| 3 | 修改方案 | 列出了涉及文件、每个文件的改动、风险 |
| 4 | 方案对比 | 给出了至少 2 个备选方案，说明了为什么选当前方案 |
| 5 | 知识讲解 | 讲了至少 1 个技术概念或原理 |

**修改后必须做的事（不可跳过）**：

| # | 检查项 | 要求 |
|---|--------|------|
| 6 | 验证方法 | 说了怎么验证修改正确、预期结果 |
| 7 | 文档沉淀 | Bug 修复→`docs/bug-records/BUG-xxx.md`；学习记录→`docs/learning/YYYY-MM-DD.md` |

### 重要说明

* **即使**收到结构化的 bug 清单（来自其他 AI 或用户自己写的），也必须用自己的视角重新分析每一层。结构化清单不是跳过流程的理由。
* **即使**自己跑 curl/读代码完成了技术排查，也必须把排查结论输出后再改。排查完成不等于流程完成。
* **例外**：只有用户明确说"直接改"、"跳过分析"时才可省略阶段 1-5。

## 当前分支

`my-old-dev` — AI 智能诊断模块开发分支
