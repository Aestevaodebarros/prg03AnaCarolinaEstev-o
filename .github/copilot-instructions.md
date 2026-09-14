# Instruções do agente para este workspace

## Contexto do projeto
- Este repositório contém projetos Java Maven, especialmente a atividade `atv07`.
- A estrutura principal da atividade é:

  src/
  └── main/
      └── java/
          └── br/
              └── com/
                  └── ifba/
                      ├── login/
                      │   └── view/
                      │       └── TelaLogin.java
                      └── usuario/
                          ├── entity/
                          │   └── Usuario.java
                          ├── validar/
                          │   └── ValidadorUsuario.java
                          └── view/
                              └── TelaCadastroUsuario.java

## Regras de trabalho
- Responda em português, salvo quando o usuário pedir outra língua.
- Prefira soluções simples, diretas e compatíveis com Java 11 + Maven.
- Sempre que possível, valide com `mvn compile` antes de afirmar que o projeto compila.
- Se a tarefa envolver GUI Swing em Codespaces/Ubuntu sem desktop, use `xvfb-run -a` para executar a aplicação.
- Quando o problema for de ambiente gráfico, diagnostique primeiro a falta de biblioteca X11 e instale o pacote necessário antes de concluir.
- Preserve os nomes de pacotes e classes conforme o padrão `br.com.ifba...`.
- Não invente classes, métodos ou imports que não existam no projeto.

## Regras de execução de app Java
- Para rodar a tela de login da atividade 07:

  ```bash
  cd /workspaces/prg03AnaCarolinaEstev-o/atv07
  xvfb-run -a mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin
  ```

- Se o ambiente não tiver interface gráfica, use `xvfb` e bibliotecas como `libxi6`, `libxtst6` e `libxrender1` quando necessário.

## Processo recomendado para solução
1. Reproduzir o problema.
2. Verificar a causa raiz.
3. Fazer a correção mínima.
4. Validar com compilação ou execução relevante.
5. Informar o resultado com evidência concreta.

## Objetivo
- Ajudar a manter o projeto funcional dentro do Codespaces e reduzir erros de ambiente, compilação e execução de interface Swing.
