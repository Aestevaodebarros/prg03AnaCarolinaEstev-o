#!/usr/bin/env bash
# Script auxiliar para abrir qualquer atividade do workspace.
# Aceita os valores: 07, 08, 09, 12, ou all.
set -e

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
TARGET="${1:-all}"

run_script() {
  local script_name="$1"
  echo "==> Executando $script_name"
  bash "$ROOT_DIR/$script_name"
}

case "$TARGET" in
  all)
    run_script "run-atividade07.sh"
    run_script "run-atividade08.sh"
    run_script "run-atividade09-10.sh"
    run_script "run-atividade12.sh"
    ;;
  07|atividade07|atividade-07)
    run_script "run-atividade07.sh"
    ;;
  08|atividade08|atividade-08)
    run_script "run-atividade08.sh"
    ;;
  09|atividade09|atividade-09|09-10|atividade09-10|atividade-09-10)
    run_script "run-atividade09-10.sh"
    ;;
  12|atividade12|atividade-12|heranca)
    run_script "run-atividade12.sh"
    ;;
  *)
    echo "Uso: $0 [07|08|09|12|all]"
    exit 1
    ;;
esac
