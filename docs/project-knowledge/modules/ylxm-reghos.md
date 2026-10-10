---
name: ylxm-reghos
description: 挂号/HIS 核心模块 —— 预约挂号、排班管理、收费退费
metadata:
  type: reference
---

# ylxm-reghos (挂号/HIS 核心模块)

## 概述

医院信息系统核心模块，负责挂号预约、排班管理、收费退费等核心业务流程。

## 技术栈

| 维度 | 技术 |
|------|------|
| 语言 | Java 1.8 |
| 框架 | Spring Boot + MyBatis-Plus |
| RPC | Dubbo |
| 消息队列 | RabbitMQ |
| 注册中心 | Nacos |

## 核心类

| 类 | 职责 |
|------|------|
| `RegisterController` | 挂号记录 CRUD (`/register`) |
| `GuahaoController` | 挂号操作 (`/guahao/guahao`) |

## 与其他模块的关系

| 关联模块 | 关系类型 | 说明 |
|----------|----------|------|
| `ylxm-customer` | Dubbo RPC | 查询患者信息 |
| `hospital-ai-service` | HTTP API (被动) | AI 挂号功能调用 HIS 接口 |

## 配置要点

* 端口：8071
* HIS API 接口供 AI 服务调用：
  * `GET /dep/list` — 科室列表
  * `POST /register` — 挂号记录管理
  * `POST /guahao/guahao` — 执行挂号

## 变更记录

| 日期 | 变更内容 | 关联 |
|------|----------|------|
| 2026-06-08 | 初始文档 | - |
