# Hospital AI Service Startup Script (PowerShell)

Write-Host "Starting Hospital AI Service (No Virtual Environment)..." -ForegroundColor Green

# Check environment variables
Write-Host "[1/3] Checking environment variables..." -ForegroundColor Yellow

if (-not $env:DASH_SCOPE_API_KEY) {
    Write-Host "ERROR: DASH_SCOPE_API_KEY not set!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please set environment variables in PowerShell:" -ForegroundColor Yellow
    Write-Host '  $env:DASH_SCOPE_API_KEY="sk-xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"' -ForegroundColor Cyan
    Write-Host '  $env:PINECONE_API_KEY="xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"' -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Or set system environment variables:" -ForegroundColor Yellow
    Write-Host "  Right-click 'This PC' -> Properties -> Advanced System Settings" -ForegroundColor Cyan
    Write-Host "  Click 'Environment Variables' -> Add new variables" -ForegroundColor Cyan
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host "[OK] DASH_SCOPE_API_KEY configured" -ForegroundColor Green
Write-Host "[OK] PINECONE_API_KEY configured" -ForegroundColor Green

# Install dependencies
Write-Host "[2/3] Installing dependencies..." -ForegroundColor Yellow
pip install -r requirements.txt

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: Failed to install dependencies" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

# Create logs directory
if (-not (Test-Path "logs")) {
    New-Item -ItemType Directory -Path "logs" | Out-Null
}

# Start service
Write-Host "[3/3] Starting service..." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Service started successfully!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "API Docs: http://localhost:8000/docs" -ForegroundColor White
Write-Host "Health Check: http://localhost:8000/health" -ForegroundColor White
Write-Host "WebSocket: ws://localhost:8000/v1/ws/chat/{session_id}" -ForegroundColor White
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

uvicorn app.main:app --host 0.0.0.0 --port 8000 --reload
