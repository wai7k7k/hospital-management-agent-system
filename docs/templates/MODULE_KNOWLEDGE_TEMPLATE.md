---
name: module-knowledge-template
description: 模块知识文档模板
metadata:
  type: reference
---

# {模块名称}

## 概述

> 这个模块是干什么的？一句话描述。

## 技术栈

| 维度 | 技术 |
|------|------|
| 语言 | Java / Python |
| 框架 | Spring Boot / FastAPI |
| 数据库 | MySQL / MongoDB |
| 消息队列 | RabbitMQ |
| 注册中心 | Nacos |

## 核心类/文件

| 类/文件 | 职责 | 关键方法 |
|----------|------|----------|
| `XxxController` | 接口层 | `list()`, `add()`, `update()` |
| `XxxService` | 业务层 | `processXxx()` |

## 数据流

> 描述一个典型请求从入口到出口的完整路径。

1. 请求进入 → XxxController
2. 参数校验 → XxxService
3. 业务处理 → ...
4. 返回响应

## 与其他模块的关系

| 关联模块 | 关系类型 | 说明 |
|----------|----------|------|
| `ylxm-customer` | Dubbo RPC | 查询患者信息 |

## 配置要点

* 端口：8071
* Nacos namespace：dev
* 关键配置项：
  * `xxx.enabled=true`

## 常见问题

* Q: ...
  A: ...

## 变更记录

| 日期 | 变更内容 | 关联 |
|------|----------|------|
| YYYY-MM-DD | 初始文档 | - |
