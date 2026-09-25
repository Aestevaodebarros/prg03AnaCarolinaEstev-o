#!/usr/bin/env bash
# Script de execução da atividade 07.
# A aplicação da tela de login usa Swing, portanto o processo é iniciado com
# Xvfb em ambientes Linux sem interface gráfica nativa.
set -e

cd "$(dirname "$0")"

PROJECT_DIR="atv07"
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
