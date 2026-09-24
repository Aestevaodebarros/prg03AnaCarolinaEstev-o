#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"

TARGET="${1:-atividade09}"

case "$TARGET" in
  09|atividade09|atividade-09|atividade09-10|09-10|atividade-09-10)
    PROJECT_DIR="atividades-09-10-autenticavel-validador"
    MAIN_CLASS="br.com.ifba.login.view.TelaLogin"
    ;;
  08|atividade08|atividade-08)
    PROJECT_DIR="atv07"
    MAIN_CLASS="br.com.ifba.login.view.TelaLogin"
    ;;
  *)
    echo "Uso: $0 [08|09]"
    echo "  08 -> atv07"
    echo "  09 -> atividades-09-10-autenticavel-validador"
    exit 1
    ;;
esac

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
