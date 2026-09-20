# ========================================
# RuoYi Project Build Script
# ========================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Building RuoYi Project" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$ErrorActionPreference = "Stop"

# Check Maven
try {
    $mvnVersion = mvn -version | Select-Object -First 1
    Write-Host "Maven: $mvnVersion" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Maven not found!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

$baseDir = "D:\hospital-management-system"
cd $baseDir

Write-Host "`nBuilding all modules from root (reactor build)..." -ForegroundColor Yellow

mvn clean install -DskipTests -U
if ($LASTEXITCODE -eq 0) {
    Write-Host "`n========================================" -ForegroundColor Green
    Write-Host "BUILD SUCCESS" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
} else {
    Write-Host "`n========================================" -ForegroundColor Red
    Write-Host "BUILD FAILED" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
}

Read-Host "`nPress Enter to exit"
