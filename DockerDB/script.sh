#!/usr/bin/env bash
set -euo pipefail

# ===== Config =====
VERSION="${MYSQL_VERSION:-9.4}"

# Carpeta base = donde está este script
BASE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd -P)"

# Autodetección de compose (soporta en BASE o en BASE/DockerDB)
DBDIR="$BASE/DockerDB"
COMPOSE=""
for p in \
  "$BASE/docker-compose.yml" \
  "$BASE/compose.yml" \
  "$DBDIR/docker-compose.yml" \
  "$DBDIR/compose.yml"
do
  [[ -f "$p" ]] && COMPOSE="$p" && DBDIR="$(dirname "$p")" && break
done

# Si no hay compose, lo creamos en DockerDB
if [[ -z "$COMPOSE" ]]; then
  mkdir -p "$DBDIR"
  COMPOSE="$DBDIR/docker-compose.yml"
fi

ENVFILE="$DBDIR/.env"
TAR="$DBDIR/mysql94.tar"     # mismo nombre que en Windows
CONTAINER="culturarte-mysql"

# ===== Checks Docker / compose =====
if ! command -v docker >/dev/null 2>&1; then
  echo "[ERROR] Docker no está en PATH"; exit 1
fi

SUDO=""
if ! docker info >/dev/null 2>&1; then
  # intenta con sudo si hace falta
  if sudo -n docker info >/dev/null 2>&1; then
    SUDO="sudo "
  else
    echo "[ERROR] Docker Engine no responde. Iniciá Docker o corré con sudo."
    exit 1
  fi
fi

# detectar comando compose (plugin o binario legacy)
if $SUDO docker compose version >/dev/null 2>&1; then
  COMPOSE_CMD=($SUDO docker compose)
elif command -v docker-compose >/dev/null 2>&1; then
  COMPOSE_CMD=($SUDO docker-compose)
else
  echo "[ERROR] Falta 'docker compose' (plugin) o 'docker-compose' (legacy)."
  exit 1
fi

# ===== Crear compose/env mínimos si faltan =====
if [[ ! -f "$COMPOSE" ]]; then
  cat >"$COMPOSE" <<EOF
services:
  db:
    image: mysql:$VERSION
    container_name: culturarte-mysql
    restart: unless-stopped
    environment:
      MYSQL_ROOT_PASSWORD: \${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: \${MYSQL_DATABASE}
      MYSQL_USER: \${MYSQL_USER}
      MYSQL_PASSWORD: \${MYSQL_PASSWORD}
      TZ: \${TZ}
    ports:
      - "\${MYSQL_PORT:-3307}:3306"
    volumes:
      - db_data:/var/lib/mysql
    command: >
      --character-set-server=utf8mb4
      --collation-server=utf8mb4_0900_ai_ci
volumes:
  db_data:
EOF
  echo "[INFO] Creado compose mínimo en $COMPOSE"
fi

if [[ ! -f "$ENVFILE" ]]; then
  cat >"$ENVFILE" <<'EOF'
MYSQL_PORT=3307
MYSQL_DATABASE=culturarte
MYSQL_USER=app
MYSQL_PASSWORD=app123
MYSQL_ROOT_PASSWORD=root
TZ=America/Montevideo
EOF
  echo "[INFO] Creado .env por defecto en $ENVFILE"
fi

# ===== Asegurar imagen mysql:9.4 (load tar si existe, si no pull) =====
if ! $SUDO docker image inspect "mysql:$VERSION" >/dev/null 2>&1; then
  if [[ -f "$TAR" ]]; then
    echo "[INFO] Cargando imagen desde $TAR ..."
    $SUDO docker load -i "$TAR"
  else
    echo "[INFO] Descargando mysql:$VERSION ..."
    $SUDO docker pull "mysql:$VERSION"
  fi
else
  echo "[OK] Imagen mysql:$VERSION ya local."
fi

# ===== Guardar/actualizar tar (best effort) =====
mkdir -p "$DBDIR"
if ! $SUDO docker save "mysql:$VERSION" -o "$TAR"; then
  echo "[WARN] No se pudo guardar $TAR (continuo)."
else
  echo "[OK] Tar actualizado: $TAR"
fi

# ===== Levantar servicio db =====
"${COMPOSE_CMD[@]}" -f "$COMPOSE" up -d db

# ===== Logs y datos de conexión =====
$SUDO docker logs --tail=50 "$CONTAINER" || true

# obtener puerto real desde .env (o 3307)
PORT="$(grep -E '^MYSQL_PORT=' "$ENVFILE" | tail -n1 | cut -d= -f2 || true)"
PORT="${PORT:-3307}"

echo
echo "[DONE] MySQL arriba."
echo " Conexión (host local): 127.0.0.1:$PORT"
echo " Usuario: app  Password: app123  DB: culturarte"
echo
