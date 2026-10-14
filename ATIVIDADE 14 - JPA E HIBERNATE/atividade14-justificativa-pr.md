# Atividade 14 - JPA e Hibernate (Parte 1) - justificativa para o PR

## Task 01 - Configuração do projeto

- `pom.xml`: adicionadas as dependências `org.hibernate:hibernate-core:5.6.15.Final`
  (provider JPA; versão 5.6.x porque ainda fala `javax.persistence`,
  compatível com o resto do projeto em Java 11) e
  `mysql:mysql-connector-java:8.0.33` (driver JDBC do banco local — troque
  pelo driver do seu banco se não for MySQL).
- `src/main/resources/META-INF/persistence.xml.exemplo`: modelo versionado,
  sem credenciais. Copie para `persistence.xml` (mesma pasta) e preencha
  `javax.persistence.jdbc.user`/`password`/`url` com os dados da sua
  conexão local. O `persistence.xml` real está no `.gitignore`.

## Task 02 - Mapeamento da entidade principal (`Usuario`)

`Usuario` é a classe central do domínio (é dela que `Aluno`, `Professor` e
`Autenticavel` dependem), então foi a escolhida para `@Entity`.

- `@Entity` + `@Table(name = "tb_usuario")`: o nome da tabela foi trocado
  porque `usuario`/`user` colide com palavra reservada em alguns bancos
  (MySQL, Postgres).
- `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` num novo
  campo `Long id`: nenhum atributo de negócio existente (login, cpf...)
  seria uma boa chave primária técnica — `id` é a chave que o banco
  controla; `login` continua sendo a chave "de negócio" usada por
  `equals()`/`hashCode()` (Atividade 15).
- `@Column(name = "data_nascimento")`: único atributo cujo nome de coluna
  precisou fugir do padrão (camelCase -> snake_case).
- `@Column(unique = true, ...)` em `cpf`, `email` e `login`: refletem
  regras que já existiam implicitamente no domínio (não fazia sentido dois
  usuários com o mesmo login, por exemplo) — agora o próprio banco garante
  isso.
- `@Embedded`/`@Embeddable` em `Endereco`: é o reflexo, no banco, da
  composição já modelada na Atividade 11 — as colunas do endereço vivem
  dentro de `tb_usuario`, sem tabela nem chave estrangeira própria, porque
  um Endereco não existe sem o Usuario a quem pertence.
- `@Enumerated(EnumType.STRING)` em `status`: grava `"ATIVO"` em vez do
  índice numérico do enum — mais legível direto no banco e resistente a
  bugs se a ordem dos valores do enum mudar no futuro.

`Aluno` e `Professor` **não** foram anotados nesta Parte 1 (o enunciado
pede só a entidade central). Mapear a herança delas (`@Inheritance`, e
decidir entre `SINGLE_TABLE`/`JOINED`/`TABLE_PER_CLASS`) fica para quando
a persistência de fato for implementada.

## Task 03 - Estrutura de pacotes e onde ficam as imagens

O pacote `usuario` já seguia `entity`/`view` (mais `interfaces`,
`repositorio` e `validar`, que já existiam de atividades anteriores) —
nenhuma reorganização foi necessária.

**Onde ficam as imagens do projeto:** em `src/main/resources/`, fora da
árvore de pacotes Java (ex.: `src/main/resources/images/`). Recursos
estáticos (imagens, ícones) não são código-fonte e não devem morar dentro
de `src/main/java/...` junto com as classes — `src/main/resources` é o
lugar padrão do Maven para esse tipo de arquivo, e é o mesmo motivo pelo
qual `META-INF/persistence.xml` também mora lá.

## Task 04 - Telas do CRUD

Três telas, só os componentes visuais (nenhum `EntityManager`,
`em.persist`/`em.remove` nesta Parte 1 — isso é a Parte 2):

- `TelaCadastroUsuario` (já existia desde a Atividade 10/15) — cadastro.
- `TelaListarUsuarios` (nova) — listagem com busca, tabela (login, nome,
  email, status) e botões Editar/Remover/Novo.
- `TelaEditarUsuario` (nova) — formulário de edição, login bloqueado
  (é a chave de identidade do objeto) e senha opcional (em branco mantém
  a atual).

**Decisão de interpretação, para ficar transparente:** as três telas usam
o `RepositorioUsuarioEmMemoria` (Atividade 15) para listar/editar/remover
de verdade, em vez de ficarem com botões sem nenhuma ação. Isso não é
"lógica de persistência" no sentido de JPA/Hibernate — é a mesma
List/Map em memória de antes. A ideia é que, na Parte 2, só o **interior**
do repositório muda (os métodos passam a chamar `EntityManager`), mas a
assinatura pública (`cadastrar`, `remover`, `listarTodos`,
`buscarPorLogin`) e as telas que já usam essa assinatura não precisam ser
reescritas. Se a expectativa da atividade for telas 100% "mudas" (sem
nenhuma ação real nos botões), é só avisar que eu ajusto.

## Task 05 - Mantendo a rede verde

A suíte de testes (30/30) continua passando depois do mapeamento — ver
`atividade14-suite-de-testes-executada.txt`. O que mudou em `Usuario` e
`Endereco` para viabilizar o JPA, e por que não quebrou nada:

- **Campos deixaram de ser `final`:** o Hibernate precisa popular os
  atributos de uma entidade já existente (vinda do banco) por reflexão,
  depois de já ter chamado o construtor vazio — o que é incompatível com
  `final`. Os testes não dependiam da imutabilidade dos campos (só dos
  getters), então nada quebrou.
- **Construtor vazio `protected Usuario()`/`protected Endereco()`:**
  exigido pelo JPA (toda `@Entity`/`@Embeddable` precisa de um). É
  `protected`, não `public`, então continua impossível criar um `Usuario`
  "vazio" por acidente fora do pacote `entity` — os testes e as telas
  continuam usando os construtores completos de sempre.
- **Novo campo `id` (`Long`):** não tinha equivalente antes; não afeta
  `equals()`/`hashCode()` (que continuam por `login`, Atividade 15) nem
  nenhum teste existente.
- **Novos setters** (`setNome`, `setCpf`, `setGenero`,
  `setDataNascimento`, `setTelefone`, `setEmail`, `setSenha`): adicionados
  só para viabilizar `TelaEditarUsuario`; não removem nem alteram nenhum
  getter/construtor usado pelos testes.

Nenhum teste precisou ser reescrito por causa do mapeamento — a suíte
inteira (Atividades 11, 12, 13 e 15) seguiu verde sem alteração.

## Task 06 - A pergunta do DTO

Respondida em `RETROSPECTIVA.md` (raiz do repositório).
