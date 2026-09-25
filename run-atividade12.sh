#!/usr/bin/env bash
# Script central para abrir a Atividade 12 no Codespaces.
# O ambiente do container não tem desktop nativo, então usamos Xvfb para
# simular uma interface gráfica para a aplicação Swing.
set -e

# Entra na pasta do projeto antes de iniciar a aplicação.
cd "$(dirname "$0")/atividade12-heranca/ATIVIDADE 12 - HERANCA"

# Executa a tela principal da atividade 12.
xvfb-run -a mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin
