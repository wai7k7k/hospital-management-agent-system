@echo off
chcp 65001 >nul
title 医院智能 AI 服务

echo.
echo ================================
echo    医院智能 AI 服务启动器
echo ================================
echo.

REM 检查 Python 是否安装
python --version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到 Python，请先安装 Python
    pause
    exit /b 1
)

REM 进入项目目录
cd /d D:\hospital-management-system\hospital-ai-service

REM 检查是否存在虚拟环境，不存在则创建
if not exist "venv" (
    echo 正在创建虚拟环境...
    python -m venv venv
)

REM 激活虚拟环境
echo 正在激活虚拟环境...
call venv\Scripts\activate

REM 安装依赖
echo 正在安装/更新依赖...
pip install -r requirements.txt -q

REM 创建日志目录
if not exist "logs" mkdir logs

echo.
echo 服务即将启动...
echo API文档: http://localhost:8000/docs
echo 健康检查: http://localhost:8000/health
echo.

REM 启动服务
uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload

pause
