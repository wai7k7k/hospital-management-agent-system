---
name: ylxm-customer
description: 患者/客户管理模块 —— 患者建档、信息查询
metadata:
  type: reference
---

# ylxm-customer (患者/客户管理模块)

## 概述

患者基本信息管理模块，提供患者建档、信息查询等功能。

## 技术栈

| 维度 | 技术 |
|------|------|
| 语言 | Java 1.8 |
| 框架 | Spring Boot + MyBatis-Plus |
| RPC | Dubbo |
| 注册中心 | Nacos |

## 与其他模块的关系

| 关联模块 | 关系类型 | 说明 |
|----------|----------|------|
| `ylxm-reghos` | Dubbo RPC (上游) | 挂号时查询患者信息 |
| `hospital-ai-service` | HTTP API (被动) | AI 服务查询患者 |

## 配置要点

* 端口：8061
* 接口供 AI 服务调用：
  * `GET /patient/list` — 患者列表
  * `GET /patient/{id}` — 患者详情

## 变更记录

| 日期 | 变更内容 | 关联 |
|------|----------|------|
| 2026-06-08 | 初始文档 | - |
