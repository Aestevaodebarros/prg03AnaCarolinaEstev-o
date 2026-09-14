# prg03[nome_do_aluno] - Sistema Academico

Projeto Java Maven com as telas de Login e Cadastro de Usuario (Atividades 04 a 07).

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

## Antes de entregar (para cada atividade)
1. Renomeie o artifactId no pom.xml para prg03[seu_nome]
2. Crie a branch feature/atividade-XX-... correspondente
3. Faca commits granulares (veja sugestoes nos slides de cada atividade)
4. git push origin feature/atividade-XX-...
5. Abra a Pull Request, revise o diff, faca merge e delete a branch
6. Tire o print pedido e coloque na pasta com a nomenclatura indicada em cada atividade
