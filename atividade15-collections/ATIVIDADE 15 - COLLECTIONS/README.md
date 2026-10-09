# prg0AnaCarolina- Sistema Academico

Projeto Java Maven com as telas de Login e Cadastro de Usuario (Atividades 04 a 07).

# Estrutura do projeto
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

## Como rodar
mvn compile exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin
(ou importe como projeto Maven na sua IDE e rode a classe TelaLogin)

## Estrutura de pacotes
- br.com.ifba.login.view      -> TelaLogin
- br.com.ifba.login.imagens   -> imagens da tela de login (logo.png etc.)
- br.com.ifba.usuario.view    -> TelaCadastroUsuario
- br.com.ifba.usuario.imagens -> imagens das telas de usuario
- br.com.ifba.usuario.validar -> ValidadorUsuario (metodo estatico)
- br.com.ifba.usuario.entity  -> Usuario (classe de dominio)

## Atividades cobertas
- [04] Projeto Java + Tela de Login (Tasks 01-03)
- [05] Tela de Cadastro de Usuario + link "Nao tenho conta? Cadastre-se" + validacoes com if/else
- [06] Classe ValidadorUsuario com metodo estatico contemPalavraProibida
- [07] Classe de dominio Usuario, instanciada nos botoes Cadastrar e Entrar

## Atividade 13 - Polimorfismo

### Task 04 - Sobrecarga onde ela faz sentido

`Usuario` tem dois construtores com o mesmo nome e listas de parâmetros
diferentes:

- `Usuario(String login, String senha)` — só os campos **obrigatórios**,
  usado na Tela de Login, onde a pessoa só digita login e senha.
- `Usuario(String nome, String cpf, String genero, String dataNascimento,
  String telefone, String email, String login, String senha)` — **todos os
  campos**, usado na Tela de Cadastro, onde o cadastro completo é
  preenchido de uma vez.

As duas versões existem porque são dois momentos reais e diferentes do
sistema (logar vs. cadastrar), cada um com um conjunto diferente de dados
disponíveis — não é sobrecarga sem motivo nem código duplicado: a versão
curta simplesmente delega para a versão completa (`this(null, null, ...,
login, senha)`), preenchendo com `null` o que ainda não existe.

### Polimorfismo via interface (Tasks 01, 02, 03 e 05)

- `Autenticavel.autenticar(String, String)` agora **devolve** uma `String`
  (a mensagem) em vez de imprimir ou devolver `boolean` (Task 02).
- `Usuario` e `Administrador` implementam `autenticar(...)` de formas
  diferentes — `Administrador` não herda de `Usuario`, é uma classe
  totalmente separada que só compartilha o contrato `Autenticavel`
  (Task 01).
- `Autenticavel.processar(Autenticavel pessoa, String login, String senha)`
  é um método estático que recebe sempre o tipo geral e nunca sabe qual
  classe concreta chegou (Task 03).
- Testes em `PolimorfismoAutenticacaoTest` comprovam que cada classe
  responde do seu próprio jeito ao mesmo método (Task 05).

## Atividade 15 - Collections (Guardando os seus objetos)

### Task 01 - Repositório em memória

`RepositorioUsuarioEmMemoria` guarda os usuários em um
`private final List<Usuario> usuarios = new ArrayList<>();`, com
`cadastrar(Usuario)` e `listarTodos()` (devolve `List<Usuario>` imutável,
via `List.copyOf`). `TelaCadastroUsuario` e `TelaLogin` agora compartilham
a mesma instância do repositório — nada mais de usuário cadastrado e
esquecido numa variável local que morre quando a tela fecha.

### Task 02 - Usuario sabe se comparar

`Usuario.equals`/`Usuario.hashCode` comparam pelo `login` (não pelo CPF):
é o campo que o próprio sistema já usa para autenticar e indexar, e nem
todo `Usuario` tem CPF preenchido (o construtor simplificado da Tela de
Login só tem login/senha). Antes dessa implementação,
`usuarios.contains(outroComMesmoLogin)` devolvia `false` mesmo com o mesmo
login — dois objetos diferentes eram "diferentes" para a lista. Depois,
devolve `true`: é a mesma lista, o mesmo método `contains`, mas a resposta
muda porque quem decide o que é "igual" é a classe `Usuario`, não a
coleção (teste: `RepositorioUsuarioEmMemoriaTest.
doisObjetosDiferentesComOMesmoLoginSaoIguaisParaALista`).

### Task 03 - Índice por login: List (for) vs. Map

`buscarPorLoginComFor(login)` percorre a `List` inteira com um `for` até
achar o login (ou chegar ao fim); `buscarPorLogin(login)` usa o
`HashMap<String, Usuario> porLogin`, alimentado a cada `cadastrar(...)`.

Com **dez usuários** a diferença é imperceptível — os dois terminam
"instantaneamente". Com **dez mil usuários**, a busca pela `List` é O(n):
no pior caso (login inexistente, ou o último da lista) o laço compara os
dez mil registros toda vez que alguém tenta entrar. A busca pelo `Map` é
O(1) em média: o `HashMap` calcula o hash do login e vai direto ao balde
certo, então o tempo de busca não cresce com o tamanho da base.
