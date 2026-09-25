# Atividade 12 - Task 01

**Hierarquia escolhida:** `Aluno` **é um** `Usuario`; `Professor` **é um** `Usuario`.

## Por que herança, e não composição?

> No domínio do SUAP-IFBA, `Aluno` e `Professor` **são** tipos de `Usuario` —
> eles compartilham toda a identidade e o comportamento de um usuário
> (nome, cpf, login, senha, endereço, status, a capacidade de se
> `autenticar(...)`), e cada um apenas **especializa** esse comportamento
> com dados e regras próprias (matrícula/curso para o Aluno, SIAPE para o
> Professor).
>
> Isso é diferente de uma relação "tem um" (composição/agregação), que é o
> que já usamos em outros pontos do mesmo domínio:
> - `Usuario` **tem um** `Endereco` (composição — o endereço não existe
>   sem o usuário a quem pertence).
> - `Aluno` **tem uma lista de** `Turma` (agregação — a turma existe
>   independentemente do aluno estar ou não matriculado nela).
>
> O teste prático para decidir foi perguntar: *"faz sentido dizer que um
> Aluno **é** um Usuario?"* — sim, sem ressalvas: todo Aluno pode fazer
> tudo que um Usuario faz (autenticar, ter status, ter endereço) e **é**
> semanticamente um usuário do sistema, não apenas algo que "usa" ou
> "contém" um usuário. Já não faria sentido o inverso ("um Usuario é um
> Aluno"), porque nem todo usuário é aluno — pode ser professor. Essa
> assimetria "é-um" de mão única é exatamente o sinal de que a relação
> correta é **herança**, com `Usuario` como superclasse comum.
>
> Se tivéssemos usado composição aqui (ex.: `Aluno` guardando um
> `Usuario usuario` como atributo), perderíamos o polimorfismo: não
> poderíamos ter uma `List<Usuario>` com Alunos e Professores misturados e
> chamar `usuario.descreverPerfil()` deixando cada objeto decidir sozinho
> como se descrever — teríamos que fazer `if (aluno) ... else if
> (professor) ...` na mão em todo lugar que precisasse tratar os dois
> tipos de forma uniforme. A herança nos dá isso de graça.

## Métodos herdado vs. sobrescrito (Task 02 do enunciado desta atividade)

- **Herdado sem alteração:** `Usuario.saudacao()` — tanto `Aluno` quanto
  `Professor` usam a implementação da classe mãe sem sobrescrever.
- **Sobrescrito (`@Override`):** `Usuario.descreverPerfil()` — cada
  subclasse devolve sua própria descrição (`Aluno` inclui matrícula e
  curso; `Professor` inclui SIAPE e quantidade de turmas).
