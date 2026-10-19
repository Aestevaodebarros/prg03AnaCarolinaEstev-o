package br.com.ifba.usuario.exception;

/**
 * Atividade 17 - Task 03: exceção do domínio "usuário".
 *
 * Lançada quando um código pede um Usuario que deveria existir (pelo
 * login) e ele não é encontrado no banco — ver
 * {@link br.com.ifba.usuario.repositorio.RepositorioUsuario#buscarPorLoginOuFalhar(String)}
 * (Task 04).
 *
 * Estende {@link RuntimeException} (não é obrigatório declarar
 * {@code throws} em todo método que pode lançá-la) de propósito: um
 * "usuário sumiu entre eu listar e eu tentar editar" é uma situação
 * excepcional e relativamente rara (outra tela removeu o mesmo usuário
 * nesse intervalo) — não é algo que toda chamada precise ser forçada a
 * tratar explicitamente, mas é algo que a tela que abre a edição
 * (Task 05) precisa capturar e mostrar numa mensagem compreensível.
 */
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException(String login) {
        super("Nenhum usuario encontrado com o login \"" + login + "\". "
                + "Ele pode ter sido removido por outra tela enquanto esta lista estava aberta.");
    }
}
