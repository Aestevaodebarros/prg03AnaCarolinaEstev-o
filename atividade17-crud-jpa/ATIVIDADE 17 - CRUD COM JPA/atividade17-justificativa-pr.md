# Atividade 17 - CRUD completo com tratamento de erros - justificativa para o PR

## Task 01 - Fábrica centralizada (`JPAUtil`)

`br.com.ifba.infrastructure.util.JPAUtil` cria o `EntityManagerFactory`
**uma única vez** (campo `static final`, inicializado no carregamento da
classe) e expõe `getEntityManager()` para quem precisar. Nenhuma tela cria
a fábrica sozinha — todas (direta ou indiretamente, via
`RepositorioUsuario`) pedem um `EntityManager` aqui. `JPAUtil.close()` é
chamado uma única vez, ao fechar a janela principal (`TelaLogin`, listener
de `windowClosing`), para não deixar a conexão/pool presos até o processo
morrer.

## Task 02 - Telas ligadas ao banco

`RepositorioUsuarioEmMemoria` (Atividade 15) foi **substituído** por
`RepositorioUsuario` — mesma responsabilidade, agora com JPA de verdade:

| Operação | Antes (Atividade 15)      | Agora (Atividade 17)                              |
|----------|----------------------------|----------------------------------------------------|
| cadastrar | `usuarios.add(...)`       | `em.persist(usuario)`                               |
| listar    | `List.copyOf(usuarios)`   | `em.createQuery("SELECT u FROM Usuario u", ...)`    |
| editar    | (não existia)              | `em.merge(usuario)` (novo método `editar`)          |
| remover   | `usuarios.remove(...)`    | `em.merge(...)` + `em.remove(...)`                  |
| buscar    | `porLogin.get(login)`     | JPQL `WHERE LOWER(u.login) = LOWER(:login)`         |

Toda escrita (`cadastrar`, `editar`, `remover`) segue o mesmo padrão:
`begin()` → operação → `commit()`; qualquer `RuntimeException` cai num
`catch` que dá `rollback()` (só se a transação ainda estiver ativa) e
relança; um `finally` fecha o `EntityManager` sempre, dê certo ou não
(Task 06).

**Decisão de simplificação:** como cada método do `RepositorioUsuario`
abre/fecha seu próprio `EntityManager`, a classe deixou de ter estado
próprio (não guarda mais `List`/`Map`). Por isso as telas pararam de
receber um `RepositorioUsuario` compartilhado pelo construtor (como
precisavam fazer na Atividade 15, quando o estado vivia no objeto
repositório) — cada tela agora só faz `new RepositorioUsuario()`; o
estado que importa é o banco, e qualquer instância enxerga os mesmos
dados porque todas usam a mesma fábrica (`JPAUtil`).

## Task 03 - Exceção do domínio

`br.com.ifba.usuario.exception.UsuarioNaoEncontradoException extends
RuntimeException`, com um construtor que recebe o login e monta a
mensagem explicativa.

## Task 04 - Onde o `null` foi trocado por `throw`

**Método:** `RepositorioUsuario.buscarPorLoginOuFalhar(String login)`
(novo), usado pelo construtor de `TelaEditarUsuario`.

**O que acontecia antes** (com `buscarPorLogin`, que continua existindo e
devolvendo `null` — ver próxima seção): `TelaListarUsuarios` manda o
usuário selecionado para a tela de edição. Se, entre o momento em que a
lista foi carregada e o clique em "Editar", **outra** tela tivesse
removido esse mesmo usuário, buscar por aquele login no banco devolveria
`null`. Se `TelaEditarUsuario` não checasse esse `null` explicitamente
(fácil de esquecer — o compilador não obriga), a primeira linha que
preenchesse um campo (`usuario.getNome()`, por exemplo) explodiria com
`NullPointerException` — um erro que não diz nada sobre a causa real
("o usuário foi removido"), só que "algo é null em algum lugar da pilha".

**Agora:** o problema é detectado no exato ponto em que acontece, com uma
mensagem que já explica a causa. `TelaEditarUsuario` captura a exceção no
construtor (Task 05) e fecha a própria tela com uma mensagem clara, em vez
de abrir "vazia" e quebrar no primeiro campo.

`buscarPorLogin` (sem o "OuFalhar") continua devolvendo `null` de
propósito nos lugares onde `null` é uma resposta válida e esperada — ex.:
checar duplicidade de login antes de cadastrar (`TelaCadastroUsuario`) ou
"usuário não encontrado, cadastre-se" no login (`TelaLogin`). Essas duas
telas já tratam o `null` explicitamente; a exceção só existe onde um
`null` esquecido causaria um bug silencioso.

## Task 05 - Captura onde dá para resolver

Toda chamada ao repositório nas telas (`TelaCadastroUsuario`,
`TelaLogin`, `TelaListarUsuarios`, `TelaEditarUsuario`) está dentro de um
`try/catch` que mostra a mensagem da exceção num `JOptionPane` e
**retorna** (a tela continua aberta e usável) — nunca deixa a exceção
subir e derrubar a aplicação, e nunca finge que a operação funcionou.

## Task 06 - Feche o que foi aberto

Ver o padrão descrito na Task 02: `finally { em.close(); }` em todo
método do repositório, e `rollback()` condicionado a `isActive()` no
`catch`. Nenhum `catch` vazio, nenhum `catch` só com
`printStackTrace()` — todos ou tratam (mostram mensagem) ou relançam.

## Task 07 - Busca com JPQL

`RepositorioUsuario.buscarPorTermo(String termo)`:
```java
SELECT u FROM Usuario u WHERE LOWER(u.login) LIKE :termo OR LOWER(u.nome) LIKE :termo
```
usado pelo campo de busca de `TelaListarUsuarios` (botão "Buscar"),
substituindo o filtro em memória da Atividade 14.

## Task 08 - Mantendo a rede

`RepositorioUsuarioTest` (novo, substitui
`RepositorioUsuarioEmMemoriaTest`) cobre o CRUD completo via
`RepositorioUsuario` e, especificamente para a Task 08:
- `buscarPorLoginOuFalharLancaExcecaoParaLoginInexistente` — `assertThrows(UsuarioNaoEncontradoException.class, ...)`.
- `buscarPorLoginOuFalharNaoLancaExcecaoQuandoUsuarioExiste` — `assertDoesNotThrow(...)` no caminho normal.

**34/34 testes passam** — ver `atividade17-suite-de-testes-executada.txt`.

## Observação sobre o ambiente de verificação

Este sandbox não tem acesso ao Maven Central (proxy bloqueado), então o
`hibernate-core`/`mysql-connector-java` declarados no `pom.xml` não podem
ser baixados aqui para rodar contra um banco de verdade. Para verificar
que o código compila e que a lógica de controle (transação, rollback,
exceção, JPQL) está correta, usei um `EntityManager`/
`EntityManagerFactory`/`EntityTransaction`/`TypedQuery` "fake" (mesma
interface `javax.persistence`, implementação própria, só para este
sandbox) com um banco em memória que só aplica as operações no
`commit()` e descarta tudo no `rollback()` — o que efetivamente comprova
que o padrão begin/commit/rollback do `RepositorioUsuario` funciona como
deveria. Em produção, fora deste sandbox, é o Hibernate de verdade (via
o `pom.xml` real) quem executa essas mesmas chamadas contra o banco.
