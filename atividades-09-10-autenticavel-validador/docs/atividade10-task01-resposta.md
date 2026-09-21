# Atividade 10 - Task 01

**Pergunta:** Por que não é possível escrever um teste JUnit para o código de
validação (CPF, campos vazios, senha) do jeito que ele estava na
`TelaCadastroUsuario` original?

**Resposta (para colar como comentário no PR):**

> As validações estavam escritas dentro de `btnCadastrarActionPerformed`,
> um `private void` que só é disparado pelo `ActionListener` do botão
> "Cadastrar". Isso impede o teste unitário por alguns motivos:
>
> 1. **O método não retorna nada e não é acessível.** É `private` e `void`;
>    um teste não tem como chamá-lo diretamente nem verificar um resultado —
>    só poderia observar efeitos colaterais (o `JOptionPane` que abre na tela).
> 2. **A lógica está acoplada ao Swing.** Para "rodar" a validação seria
>    preciso instanciar `TelaCadastroUsuario` (um `JFrame`), preencher os
>    `JTextField`/`JPasswordField` via reflection ou simulando cliques, e
>    depois inspecionar um `JOptionPane` que abriu — algo lento, frágil e que
>    normalmente trava em ambiente sem display gráfico (CI, Codespaces sem
>    Xvfb).
> 3. **Regra de negócio e efeito colateral (UI) estão misturados.** O mesmo
>    bloco decide "o CPF é válido?" e também decide "o que mostrar na tela" —
>    não dá para testar uma coisa sem a outra.
> 4. **Não existe uma unidade isolada e determinística para chamar.** JUnit
>    testa uma função com entrada e saída previsíveis (`assertTrue`/
>    `assertFalse`); aqui a "saída" é uma janela do sistema operacional.
>
> A solução (Task 02) foi extrair cada regra para métodos estáticos puros em
> `ValidadorUsuario` (`camposPreenchidos`, `cpfValido`, `senhaForte`,
> `senhasConferem`), que recebem `String` e devolvem `boolean`. Isso os torna
> triviais de testar com JUnit, sem precisar de Swing, display gráfico ou
> qualquer estado externo. O listener do botão virou só "orquestrador":
> captura o texto dos campos, chama os métodos do validador e exibe a
> mensagem correspondente.
