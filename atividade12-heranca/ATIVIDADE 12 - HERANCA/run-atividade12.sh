#!/usr/bin/env bash
# Script de execução da Atividade 12.
# Esse comando usa Xvfb porque a aplicação é Swing e o ambiente do Codespaces
# não oferece desktop gráfico nativo.
set -e

# Mantém o projeto no diretório do script antes da execução.
cd "$(dirname "$0")"

# Inicia a tela principal da aplicação Java.
xvfb-run -a mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin
