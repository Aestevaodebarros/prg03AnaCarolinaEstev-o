# AGENTS

## Visão geral
Este workspace é usado para desenvolver e validar projetos Java Maven dentro do Codespaces. O agente deve priorizar correções práticas, validação real e compatibilidade com ambientes sem desktop gráfico.

## Diretrizes
- Trabalhe em português.
- Concentre-se em soluções para Java, Maven e Swing.
- Sempre que houver GUI no Linux, considere o uso de `xvfb`.
- Quando o usuário pedir para rodar uma aplicação Java, valide a execução com o comando mais direto e adequado ao ambiente.
- Antes de afirmar que algo está funcionando, execute a checagem relevante: compilação, execução ou teste.
- Preserve a estrutura de pacotes do projeto e os nomes das classes.

## Comandos úteis
```bash
cd /workspaces/prg03AnaCarolinaEstev-o/atv07
mvn compile
xvfb-run -a mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin
```

## Foco do projeto atual
- A atividade `atv07` contém a tela de login e o cadastro de usuário.
- A tela principal é `br.com.ifba.login.view.TelaLogin`.
- O agente deve evitar quebrar a organização do projeto e preferir ajustes mínimos que mantenham a aplicação executável.
