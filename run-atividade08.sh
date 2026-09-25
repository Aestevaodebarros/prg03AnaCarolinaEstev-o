#!/usr/bin/env bash
# Script de execução do conjunto de atividades com interface Swing.
# No Codespaces, a aplicação é aberta em um servidor virtual X para que o
# Java/Swing possa iniciar sem um desktop gráfico local.
set -e

cd "$(dirname "$0")"

TARGET="${1:-atividade08}"

case "$TARGET" in
  07|atividade07|atividade-07)
    PROJECT_DIR="atv07"
    MAIN_CLASS="br.com.ifba.login.view.TelaLogin"
    ;;
  08|atividade08|atividade-08|encapsulamento)
    PROJECT_DIR="prg03-atividade08-encapsulamento (1)"
    MAIN_CLASS="br.com.ifba.login.view.TelaLogin"
    ;;
  09|atividade09|atividade-09|atividade09-10|09-10|atividade-09-10)
    PROJECT_DIR="atividades-09-10-autenticavel-validador"
    MAIN_CLASS="br.com.ifba.login.view.TelaLogin"
    ;;
  12|atividade12|atividade-12|heranca)
    PROJECT_DIR="atividade12-heranca/ATIVIDADE 12 - HERANCA"
    MAIN_CLASS="br.com.ifba.login.view.TelaLogin"
    ;;
  *)
    echo "Uso: $0 [07|08|09|12]"
    echo "  07 -> atv07"
    echo "  08 -> prg03-atividade08-encapsulamento (1)"
    echo "  09 -> atividades-09-10-autenticavel-validador"
    echo "  12 -> atividade12-heranca/ATIVIDADE 12 - HERANCA"
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
