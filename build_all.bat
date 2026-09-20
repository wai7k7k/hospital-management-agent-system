@echo off
chcp 65001 > nul
echo ========================================
echo Building RuoYi Project (Reactor Build)
echo ========================================

cd /d D:\hospital-management-system

echo.
echo Building all modules from root (Maven reactor handles ordering)...
call mvn clean install -DskipTests -U
if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Build failed!
    pause
    exit /b 1
)

echo.
echo ========================================
echo All modules build successfully!
echo ========================================
pause
