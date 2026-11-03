# Atividade 19 - Camada de acesso a dados

## Task 01 - Superclasse das entidades

`br.com.ifba.infrastructure.entity.PersistenceEntity` concentra o `Long id`,
anotado com `@Id` e `@GeneratedValue`, e está mapeada com `@MappedSuperclass`.
`Usuario` herda essa classe e não declara mais seu próprio identificador. As
classes `Aluno` e `Professor` herdam de `Usuario`, portanto também recebem o
identificador. No projeto, `Usuario` é a única classe anotada com `@Entity`.

## Task 02 - Contrato genérico

`br.com.ifba.infrastructure.dao.GenericDao<Entity extends PersistenceEntity>`
declara `save`, `update`, `delete`, `findById` e `findAll`, permitindo que o
contrato seja reutilizado pelas entidades que compartilham a classe-base.

## Task 03 - Implementação do DAO

`GenericDaoImpl` recebe a classe concreta da entidade no construtor para
executar `findById` e montar a consulta genérica de `findAll`. As operações de
escrita iniciam e confirmam uma transação; em caso de falha fazem rollback se
a transação ainda estiver ativa, encapsulam a causa em `PersistenciaException`
e sempre fecham o `EntityManager` no `finally`. As consultas também fecham o
`EntityManager` no `finally`.

`RepositorioUsuario` estende `GenericDaoImpl<Usuario>` e delega o CRUD comum
ao DAO genérico, preservando as consultas específicas por login e termo.

## Validação

O perfil de teste usa H2 em memória por meio de
`src/test/resources/META-INF/persistence.xml`; a configuração de produção e
`persistence.xml.exemplo` não foram alterados. `mvn test` executou 35 testes,
com zero falhas ou erros. A saída está registrada em
`atividade19-suite-de-testes-executada.txt`.