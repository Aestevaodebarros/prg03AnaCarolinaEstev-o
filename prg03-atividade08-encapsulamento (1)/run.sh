#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"

if command -v xvfb-run >/dev/null 2>&1; then
  xvfb-run -a mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin
else
  echo "xvfb-run nao encontrado. Instale com: sudo apt-get install -y xvfb"
  exit 1
fi
