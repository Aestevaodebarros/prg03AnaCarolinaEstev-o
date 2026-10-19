package br.com.ifba.usuario.repositorio;

import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.exception.UsuarioNaoEncontradoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Atividade 17 - Task 08 ("mantenha a rede"): testes da regra que dispara
 * {@link UsuarioNaoEncontradoException}, além do CRUD básico via
 * {@link RepositorioUsuario} (persist/merge/remove/JPQL).
 *
 * Cada teste usa um login único (gerado por {@link #loginUnico()}) porque
 * o EntityManagerFactory de JPAUtil é compartilhado por toda a suíte — um
 * "banco" só, assim como aconteceria com um banco de verdade entre vários
 * testes que não se isolam em transações próprias.
 */
class RepositorioUsuarioTest {

    private final RepositorioUsuario repositorio = new RepositorioUsuario();

    private static int contador = 0;

    private String loginUnico() {
        return "usuario_teste_" + (contador++);
    }

    @Test
    void cadastrarGravaEListarTodosTraz() {
        String login = loginUnico();
        Usuario usuario = new Usuario("Fulano de Tal", "11122233344", "Masculino",
                "1999-01-01", "77988887777", "fulano@ifba.edu.br", login, "senhaForte1");

        repositorio.cadastrar(usuario);

        assertTrue(repositorio.listarTodos().stream().anyMatch(u -> u.getLogin().equals(login)));
    }

    @Test
    void buscarPorLoginEncontraQuemFoiCadastrado() {
        String login = loginUnico();
        repositorio.cadastrar(new Usuario("Ciclana", "22233344455", "Feminino",
                "2000-02-02", "77977776666", "ciclana@ifba.edu.br", login, "senhaForte1"));

        Usuario encontrado = repositorio.buscarPorLogin(login);

        assertEquals(login, encontrado.getLogin());
        assertEquals("Ciclana", encontrado.getNome());
    }

    @Test
    void buscarPorLoginComLoginInexistenteDevolveNulo() {
        assertNull(repositorio.buscarPorLogin("login_que_nunca_existiu_12345"));
    }

    /**
     * Task 08 - "verifique com assertThrows que ela é lançada quando deve".
     */
    @Test
    void buscarPorLoginOuFalharLancaExcecaoParaLoginInexistente() {
        UsuarioNaoEncontradoException excecao = assertThrows(UsuarioNaoEncontradoException.class,
                () -> repositorio.buscarPorLoginOuFalhar("login_que_nunca_existiu_12345"));

        assertTrue(excecao.getMessage().contains("login_que_nunca_existiu_12345"));
    }

    /**
     * Task 08 - "e que não é lançada no caminho normal".
     */
    @Test
    void buscarPorLoginOuFalharNaoLancaExcecaoQuandoUsuarioExiste() {
        String login = loginUnico();
        repositorio.cadastrar(new Usuario("Beltrano", "33344455566", "Masculino",
                "1998-03-03", "77966665555", "beltrano@ifba.edu.br", login, "senhaForte1"));

        assertDoesNotThrow(() -> repositorio.buscarPorLoginOuFalhar(login));
    }

    @Test
    void editarAtualizaOsCamposNoBanco() {
        String login = loginUnico();
        Usuario usuario = new Usuario("Nome Antigo", "44455566677", "Masculino",
                "1997-04-04", "77955554444", "antigo@ifba.edu.br", login, "senhaForte1");
        repositorio.cadastrar(usuario);

        usuario.setNome("Nome Novo");
        usuario.setEmail("novo@ifba.edu.br");
        repositorio.editar(usuario);

        Usuario recarregado = repositorio.buscarPorLoginOuFalhar(login);
        assertEquals("Nome Novo", recarregado.getNome());
        assertEquals("novo@ifba.edu.br", recarregado.getEmail());
    }

    @Test
    void removerFazOUsuarioSumirDoBanco() {
        String login = loginUnico();
        Usuario usuario = new Usuario("Para Remover", "55566677788", "Feminino",
                "1996-05-05", "77944443333", "remover@ifba.edu.br", login, "senhaForte1");
        repositorio.cadastrar(usuario);
        assertTrue(repositorio.listarTodos().stream().anyMatch(u -> u.getLogin().equals(login)));

        repositorio.remover(usuario);

        assertNull(repositorio.buscarPorLogin(login));
        assertFalse(repositorio.listarTodos().stream().anyMatch(u -> u.getLogin().equals(login)));
    }

    @Test
    void buscarPorTermoEncontraPorLoginOuPorNome() {
        String login = loginUnico();
        repositorio.cadastrar(new Usuario("Joana Termo", "66677788899", "Feminino",
                "1995-06-06", "77933332222", "joana@ifba.edu.br", login, "senhaForte1"));

        assertTrue(repositorio.buscarPorTermo("joana termo").stream()
                .anyMatch(u -> u.getLogin().equals(login)));
        assertTrue(repositorio.buscarPorTermo(login).stream()
                .anyMatch(u -> u.getLogin().equals(login)));
    }
}
