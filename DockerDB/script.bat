@echo off
setlocal EnableExtensions

rem === Rutas absolutas ===
set "BASE=C:\Users\Juanpi\Desktop\culturarte"
set "DBDIR=%BASE%\DockerDB"
set "COMPOSE=%DBDIR%\docker-compose.yml"
set "ENVFILE=%DBDIR%\.env"
set "TAR=%DBDIR%\mysql94.tar"
set "VERSION=9.4"

echo [INFO] Base   : %BASE%
echo [INFO] DBDIR  : %DBDIR%
echo [INFO] Compose: %COMPOSE%
echo [INFO] .env   : %ENVFILE%

docker --version >nul 2>&1 || (echo [ERROR] Docker no esta en PATH & goto END)
docker info >nul 2>&1 || (echo [ERROR] Docker Desktop no esta "Running" & goto END)
docker compose version >nul 2>&1 || (echo [ERROR] Falta el plugin "docker compose" & goto END)

if not exist "%DBDIR%" (
  echo [INFO] Creando %DBDIR% ...
  mkdir "%DBDIR%" || (echo [ERROR] No pude crear %DBDIR% & goto END)
)

if not exist "%COMPOSE%" (
  echo [INFO] Creando docker-compose.yml minimo...
  >"%COMPOSE%" (
    echo services:
    echo ^  db:
    echo ^    image: mysql:%VERSION%
    echo ^    container_name: culturarte-mysql
    echo ^    restart: unless-stopped
    echo ^    environment:
    echo ^      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
    echo ^      MYSQL_DATABASE: ${MYSQL_DATABASE}
    echo ^      MYSQL_USER: ${MYSQL_USER}
    echo ^      MYSQL_PASSWORD: ${MYSQL_PASSWORD}
    echo ^      TZ: ${TZ}
    echo ^    ports:
    echo ^      - "${MYSQL_PORT:-3307}:3306"
    echo ^    volumes:
    echo ^      - db_data:/var/lib/mysql
    echo ^    command: ^>
    echo ^      --character-set-server=utf8mb4
    echo ^      --collation-server=utf8mb4_0900_ai_ci
    echo volumes:
    echo ^  db_data:
  )
)

if not exist "%ENVFILE%" (
  echo [INFO] Creando .env por defecto...
  >"%ENVFILE%" (
    echo MYSQL_PORT=3307
    echo MYSQL_DATABASE=culturarte
    echo MYSQL_USER=app
    echo MYSQL_PASSWORD=app123
    echo MYSQL_ROOT_PASSWORD=root
    echo TZ=America/Montevideo
  )
)

echo [INFO] Asegurando imagen mysql:%VERSION% ...
docker image inspect mysql:%VERSION% >nul 2>&1 || docker pull mysql:%VERSION% || (echo [ERROR] pull fallo & goto END)

echo [INFO] Guardando tar en %TAR% ...
if exist "%TAR%" del /f /q "%TAR%" >nul 2>&1
docker save mysql:%VERSION% -o "%TAR%" || echo [WARN] No pude guardar el tar (continuo)

echo [INFO] Levantando MySQL...
docker compose -f "%COMPOSE%" up -d db || (echo [ERROR] compose up fallo & goto END)

echo [LOG] Ultimas lineas:
docker logs --tail=50 culturarte-mysql

echo.
echo [OK] Conectate a 127.0.0.1:%MYSQL_PORT% (ver %ENVFILE%) user=app pass=app123 db=culturarte

:END
echo.
echo [PAUSA] Pulsa una tecla para cerrar...
pause >nul
endlocal
