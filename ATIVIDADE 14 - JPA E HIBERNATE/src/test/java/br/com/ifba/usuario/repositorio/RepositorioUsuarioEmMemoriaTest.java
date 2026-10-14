package br.com.ifba.usuario.repositorio;

import br.com.ifba.usuario.entity.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Atividade 15 - Task 04 ("um teste por forma").
 */
class RepositorioUsuarioEmMemoriaTest {

    private RepositorioUsuarioEmMemoria repositorio;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioUsuarioEmMemoria();
    }

    @Test
    void cadastraUmUsuarioEEleApareceEmListarTodos() {
        Usuario usuario = new Usuario("ana", "1234");

        repositorio.cadastrar(usuario);

        assertEquals(1, repositorio.listarTodos().size());
        assertTrue(repositorio.listarTodos().contains(usuario));
    }

    @Test
    void cadastraDoisEBuscarPorLoginDevolveOCerto() {
        Usuario ana = new Usuario("ana", "1234");
        Usuario bruno = new Usuario("bruno", "5678");

        repositorio.cadastrar(ana);
        repositorio.cadastrar(bruno);

        assertEquals(ana, repositorio.buscarPorLogin("ana"));
        assertEquals(bruno, repositorio.buscarPorLogin("bruno"));

        // as duas versões da busca (Task 03) precisam concordar
        assertEquals(ana, repositorio.buscarPorLoginComFor("ana"));
        assertEquals(bruno, repositorio.buscarPorLoginComFor("bruno"));
    }

    @Test
    void buscarPorLoginComUmLoginQueNaoExisteDevolveNulo() {
        repositorio.cadastrar(new Usuario("ana", "1234"));

        assertNull(repositorio.buscarPorLogin("naoexiste"));
        assertNull(repositorio.buscarPorLoginComFor("naoexiste"));
    }

    /**
     * Task 02 em ação: antes de Usuario ter equals/hashCode por login, dois
     * objetos diferentes com o mesmo login seriam tratados como distintos
     * por qualquer coleção (contains teria dado false). Com equals/hashCode
     * implementados, a List (e o HashMap) enxergam os dois como "o mesmo
     * usuário".
     */
    @Test
    void doisObjetosDiferentesComOMesmoLoginSaoIguaisParaALista() {
        Usuario original = new Usuario("ana", "1234");
        repositorio.cadastrar(original);

        Usuario outraInstanciaMesmoLogin = new Usuario("ana", "outraSenhaQualquer");

        assertTrue(repositorio.listarTodos().contains(outraInstanciaMesmoLogin));
        assertEquals(original, outraInstanciaMesmoLogin);
    }
}
