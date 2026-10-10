---
name: ruoyi-gateway
description: API 网关 —— 统一入口、鉴权、限流、路由转发
metadata:
  type: reference
---

# ruoyi-gateway (API 网关)

## 概述

基于 Spring Cloud Gateway 的统一 API 网关，负责鉴权、限流、路由转发、CORS、XSS 过滤。

## 路由配置

网关的路由规则定义在 **Nacos 配置中心**（`ruoyi-gateway.yml`），不在源码中。

## 关键过滤器

| 过滤器 | 职责 |
|--------|------|
| `AuthFilter` | 全局鉴权 |
| `ValidateCodeFilter` | 验证码校验 |
| `BlackListUrlFilter` | URL 黑名单 |
| `XssFilter` | XSS 防护 |
| `GlobalCorsFilter` | 跨域处理 |
| `GlobalCacheRequestFilter` | 请求缓存 |
| `ForwardAuthFilter` | 认证转发 |

## 路由规则说明

* `/code` → 验证码（直接处理）
* `/prod-api/**` → Nacos 中配置的动态路由
* `/api/ai/**` → 经 Nginx 直接到 AI 服务，不经过网关

## 配置要点

* 端口：8080
* Nacos: `localhost:8848`, namespace: `dev`
* Nacos 配置: `application-common.yml` + `ruoyi-gateway.yml`

## 变更记录

| 日期 | 变更内容 | 关联 |
|------|----------|------|
| 2026-06-08 | 初始文档 | - |
