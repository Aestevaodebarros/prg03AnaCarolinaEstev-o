#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"

PROJECT_DIR="atividades-09-10-autenticavel-validador"
MAIN_CLASS="br.com.ifba.login.view.TelaLogin"

if [[ ! -d "$PROJECT_DIR" ]]; then
  echo "Diretório não encontrado: $PROJECT_DIR"
  exit 1
fi

cd "$PROJECT_DIR"

if command -v xvfb-run >/dev/null 2>&1; then
  xvfb-run -a mvn exec:java -Dexec.mainClass="$MAIN_CLASS"
else
  echo "xvfb-run não encontrado. Instale com: sudo apt-get install -y xvfb"
  exit 1
fi
