#!/bin/bash
cd "$(dirname "$0")"

xvfb-run -a mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin "$@"
