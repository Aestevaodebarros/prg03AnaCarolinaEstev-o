# Auditoria SOLID — Atividade 21

Escopo analisado: `ATIVIDADE 19 - DAO`.

## Task 01 — Exemplos dos cinco princípios

| Princípio | Exemplo concreto |
|---|---|
| **S — Responsabilidade única** | `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/usuario/validar/ValidadorUsuario.java:9` concentra as validações de dados do usuário, como CPF, senha e campos obrigatórios, separadas das telas. |
| **O — Aberto/fechado** | `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/usuario/interfaces/Autenticavel.java:12` define o contrato de autenticação. Uma nova classe pode implementar esse contrato — como `Administrador` em `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/usuario/entity/Administrador.java:20` — sem alterar `Autenticavel.processar`. |
| **L — Substituição de Liskov** | `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/usuario/entity/Aluno.java:20` estende `Usuario`; `ATIVIDADE 19 - DAO/src/test/java/br/com/ifba/usuario/entity/UsuarioHerancaTest.java:40` guarda um `Aluno` em uma referência `Usuario` e verifica o comportamento herdado pela referência base. |
| **I — Segregação de interfaces** | `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/usuario/interfaces/Autenticavel.java:12` é um contrato pequeno e focado em autenticação; classes não precisam implementar operações de persistência ou de interface gráfica para serem autenticáveis. |
| **D — Inversão de dependência** | `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/usuario/repositorio/RepositorioUsuario.java:28` depende do contrato `GenericDao<Usuario>`, não herda da implementação concreta. `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/infrastructure/dao/GenericDaoImpl.java:22` também recebe o provedor de `EntityManager` por abstração (`Supplier<EntityManager>`). |

## Task 02 — Violações encontradas

1. **DIP — dependência do repositório em implementação concreta.** Antes da correção, `RepositorioUsuario` estendia `GenericDaoImpl<Usuario>`, herdando diretamente uma classe concreta. Isso impedia trocar ou simular o DAO genérico sem mudar a hierarquia. Foi corrigido: agora implementa `GenericDao<Usuario>` e delega as operações a uma instância injetada.
2. **DIP — criação fixa do EntityManager no DAO genérico.** Antes da correção, todos os métodos de `GenericDaoImpl` chamavam `JPAUtil.getEntityManager()` diretamente. Isso acoplava a lógica do DAO ao provedor concreto. Foi corrigido com um `Supplier<EntityManager>` injetável; o construtor existente continua fornecendo o padrão JPA da aplicação.
3. **SRP — a tela de cadastro coordena responsabilidades diferentes.** Em `ATIVIDADE 19 - DAO/src/main/java/br/com/ifba/usuario/view/TelaCadastroUsuario.java:105`, o listener lê os campos, valida dados, cria `Usuario`, consulta duplicidade, persiste e apresenta mensagens. A validação está parcialmente separada em `ValidadorUsuario`, mas o fluxo de cadastro ainda mistura apresentação e coordenação do caso de uso. Essa terceira violação foi registrada, mas não alterada nesta entrega para manter as duas correções selecionadas pequenas e comportamentais.

## Task 03 — Duas correções

- **Repositório → DAO por interface (DIP):** `RepositorioUsuario` agora implementa `GenericDao<Usuario>` e delega as operações genéricas por composição. O construtor sem argumentos foi mantido e cria o DAO padrão; há também construtor para injeção, mantendo a API usada pelas telas.
- **DAO → provedor por abstração (DIP):** `GenericDaoImpl` agora recebe `Supplier<EntityManager>` e usa esse provedor nas operações. O construtor anterior permanece compatível e escolhe `JPAUtil::getEntityManager`. Foi incluído teste para verificar que o provedor injetado é usado.

## Task 04 — Renomes

Foram renomeados identificadores para tornar sua intenção mais clara, com as referências correspondentes atualizadas:

- `GenericDaoImpl.type` → `entityClass`;
- `RepositorioUsuarioTest.contador` → `proximoSufixoLogin`;
- `RepositorioUsuarioTest.loginUnico()` → `gerarLoginUnico()`;
- `RepositorioUsuario.buscarPorLogin` variável `resultado` → `usuariosEncontrados`;
- `TelaCadastroUsuario.btnCadastrarActionPerformed` → `cadastrarUsuario`;
- parâmetro `e` do listener de cadastro → `evento`.

## Task 05 — Comentário

Removido de `TelaCadastroUsuario` o comentário que apenas narrava a função do listener de cadastro e renomeado o handler para `cadastrarUsuario`, que expressa melhor sua intenção. Os comentários que explicam regras de validação ou decisões de persistência foram mantidos.

## Task 06 — Testes

Executar `mvn test` no diretório `ATIVIDADE 19 - DAO`. O resultado final está em `ATIVIDADE 21 - SOLID/print-testes-mvn-test.txt`.
