# Prog03 - Sistema Academico

Projeto Java Maven com as telas de Login e Cadastro de Usuario (Atividades 04 a 07).

## Como rodar
mvn compile
mvn exec:java -Dexec.mainClass=br.com.ifba.login.view.TelaLogin

(ou importe como projeto Maven na sua IDE e rode a classe TelaLogin)

## Estrutura de pacotes
- br.com.ifba.login.view        -> TelaLogin
- br.com.ifba.usuario.view      -> TelaCadastroUsuario
- br.com.ifba.usuario.validar   -> ValidadorUsuario
- br.com.ifba.usuario.entity    -> Usuario

## Atividades cobertas
- [04] Projeto Java + Tela de Login
- [05] Tela de Cadastro de Usuario + link "Nao tenho conta? Cadastre-se" + validacoes com if/else
- [06] Classe ValidadorUsuario com metodo estatico contemPalavraProibida
- [07] Classe de dominio Usuario, instanciada nos botoes Cadastrar e Entrar

## Estrutura do projeto
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

