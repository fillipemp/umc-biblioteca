# ============================================================
# Script de inicialização — Sistema de Biblioteca
# ============================================================
# Uso: clique com botão direito → "Executar com PowerShell"
#      ou abra PowerShell e execute: .\iniciar.ps1
# ============================================================

$ErrorActionPreference = "Continue"
$root = $PSScriptRoot

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   SISTEMA DE BIBLIOTECA - Inicializando" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Configura Java 24 e Maven
$env:JAVA_HOME = "C:\Program Files\Java\jdk-24"
$env:PATH      = "C:\Tools\maven\bin;C:\Program Files\Java\jdk-24\bin;$env:PATH"
$javaExe       = "C:\Program Files\Java\jdk-24\bin\java.exe"
$jarExe        = "C:\Program Files\Java\jdk-24\bin\jar.exe"
$javacExe      = "C:\Program Files\Java\jdk-24\bin\javac.exe"

# ── 0. Garante que o patch NIO existe em C:\tools\ (caminho sem espaços) ──
$patchJar     = "$root\biblioteca\.patch\javapatch.jar"
$patchJarDest = "C:\tools\javapatch.jar"

if (-not (Test-Path $patchJarDest)) {
    Write-Host "[0/2] Criando patch NIO para Java 24..." -ForegroundColor Yellow

    New-Item -ItemType Directory -Path "C:\Temp\patch\sun\nio\ch" -Force | Out-Null
    New-Item -ItemType Directory -Path "$root\biblioteca\.patch"      -Force | Out-Null
    New-Item -ItemType Directory -Path "C:\tools"                  -Force | Out-Null

    # Extrai módulo java.base
    & "C:\Program Files\Java\jdk-24\bin\jimage.exe" extract `
        --dir "C:\Temp\jdk_out" `
        "C:\Program Files\Java\jdk-24\lib\modules" 2>$null

    # Copia e patcha classes (iconst_1 → iconst_0 no construtor PipeImpl)
    function Patch-Class($src, $dst) {
        Copy-Item $src $dst
        $bytes = [System.IO.File]::ReadAllBytes($dst)
        for ($i = 0; $i -lt ($bytes.Length - 5); $i++) {
            if ($bytes[$i]   -eq 0x59 -and $bytes[$i+1] -eq 0x2B -and
                $bytes[$i+2] -eq 0x04 -and $bytes[$i+3] -eq 0x03 -and
                $bytes[$i+4] -eq 0xB7) {
                $bytes[$i+2] = 0x03
            }
        }
        [System.IO.File]::WriteAllBytes($dst, $bytes)
    }

    Patch-Class "C:\Temp\jdk_out\java.base\sun\nio\ch\WEPollSelectorImpl.class" `
                "C:\Temp\patch\sun\nio\ch\WEPollSelectorImpl.class"
    Patch-Class "C:\Temp\jdk_out\java.base\sun\nio\ch\WindowsSelectorImpl.class" `
                "C:\Temp\patch\sun\nio\ch\WindowsSelectorImpl.class"

    & $jarExe cf "C:\Temp\javapatch.jar" -C "C:\Temp\patch" . 2>$null
    Copy-Item "C:\Temp\javapatch.jar" $patchJar
    Copy-Item "C:\Temp\javapatch.jar" $patchJarDest
    Write-Host "      Patch criado." -ForegroundColor Green
} elseif (-not (Test-Path $patchJar)) {
    # Patch já existe em C:\tools\ mas não na pasta do projeto — copia
    New-Item -ItemType Directory -Path "$root\biblioteca\.patch" -Force | Out-Null
    Copy-Item $patchJarDest $patchJar
}

# JDK_JAVA_OPTIONS: lido pelo JVM automaticamente (evita problema de espaços no path)
$env:JDK_JAVA_OPTIONS = "--patch-module java.base=$patchJarDest"

# ── 1. MongoDB local ──────────────────────────────────────────
Write-Host "[1/2] Iniciando MongoDB local..." -ForegroundColor Yellow

$mongod = Get-Process mongod -ErrorAction SilentlyContinue
if ($mongod) {
    Write-Host "      MongoDB ja esta rodando (PID $($mongod.Id))" -ForegroundColor Green
} else {
    $mongoBin = "C:\Tools\mongodb-win32-x86_64-windows-7.0.17\bin\mongod.exe"
    if (-not (Test-Path $mongoBin)) {
        Write-Host "      AVISO: MongoDB nao encontrado em $mongoBin" -ForegroundColor Red
        Write-Host "      Baixe em: https://www.mongodb.com/try/download/community" -ForegroundColor Red
    } else {
        New-Item -ItemType Directory -Path "C:\data\db" -Force | Out-Null
        Start-Process -FilePath $mongoBin `
            -ArgumentList "--dbpath C:\data\db --port 27017 --logpath C:\data\mongod.log --logappend" `
            -WindowStyle Hidden
        Start-Sleep -Seconds 2
        Write-Host "      MongoDB iniciado em localhost:27017" -ForegroundColor Green
    }
}

# ── 2. Spring Boot ────────────────────────────────────────────
Write-Host "[2/2] Iniciando aplicacao Spring Boot..." -ForegroundColor Yellow
Write-Host ""
Write-Host "  Acesse em: http://localhost:8080" -ForegroundColor Green
Write-Host "  Login:     admin@admin.com" -ForegroundColor White
Write-Host "  Senha:     Admin@123" -ForegroundColor White
Write-Host ""
Write-Host "  Pressione Ctrl+C para parar o servidor." -ForegroundColor Gray
Write-Host ""

Set-Location "$root\biblioteca"
& "C:\tools\maven\bin\mvn.cmd" spring-boot:run
