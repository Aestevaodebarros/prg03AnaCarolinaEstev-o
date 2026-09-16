#!/usr/bin/env bash
set -e

cd "$(dirname "$0")"

export DISPLAY=${DISPLAY:-:99}

if ! command -v Xvfb >/dev/null 2>&1; then
  echo "Xvfb nao encontrado. Instale com: sudo apt-get install -y xvfb"
  exit 1
fi

if ! command -v fluxbox >/dev/null 2>&1; then
  echo "fluxbox nao encontrado. Instale com: sudo apt-get install -y fluxbox"
  exit 1
fi

if ! command -v x11vnc >/dev/null 2>&1; then
  echo "x11vnc nao encontrado. Instale com: sudo apt-get install -y x11vnc"
  exit 1
fi

if ! command -v websockify >/dev/null 2>&1; then
  echo "websockify nao encontrado. Instale com: sudo apt-get install -y novnc websockify"
  exit 1
fi

if ! pgrep -x Xvfb >/dev/null 2>&1; then
  Xvfb "$DISPLAY" -screen 0 1280x800x24 >/tmp/xvfb-gui.log 2>&1 &
  echo "Xvfb iniciado em $DISPLAY"
fi

for i in $(seq 1 30); do
  if xdpyinfo -display "$DISPLAY" >/dev/null 2>&1; then
    break
  fi
  sleep 0.5
done

if ! xdpyinfo -display "$DISPLAY" >/dev/null 2>&1; then
  echo "Nao foi possivel conectar ao display $DISPLAY"
  exit 1
fi

if ! pgrep -x fluxbox >/dev/null 2>&1; then
  fluxbox >/tmp/fluxbox-gui.log 2>&1 &
  echo "fluxbox iniciado"
fi

if ! pgrep -x x11vnc >/dev/null 2>&1; then
  x11vnc -display "$DISPLAY" -forever -nopw -shared >/tmp/x11vnc-gui.log 2>&1 &
  echo "x11vnc iniciado em 5900"
fi

if ! pgrep -x websockify >/dev/null 2>&1; then
  websockify -D --web=/usr/share/novnc/ 6080 localhost:5900 >/tmp/websockify-gui.log 2>&1 &
  echo "websockify iniciado em 6080"
fi

for i in $(seq 1 30); do
  if ss -lnt | grep -q ':6080 '; then
    break
  fi
  sleep 0.5
done

if ! ss -lnt | grep -q ':6080 '; then
  echo "Porta 6080 nao ficou ativa"
  exit 1
fi

echo "Ambiente GUI pronto. Abra a porta 6080 no Codespace e acesse /vnc.html"
echo "Executando aplicacao Java..."

mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin
