package br.com.ifba.usuario.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Atividade 11 - Task 05: testes do status (enum, Task 04) e do endereco
 * (composição, Task 02) em Usuario.
 */
class UsuarioStatusEnderecoTest {

    @Test
    void usuarioRecemCriadoNasceComStatusAtivo() {
        Usuario usuario = new Usuario("aluno1", "123456");
        assertEquals(StatusUsuario.ATIVO, usuario.getStatus());
    }

    @Test
    void usuarioSemEnderecoDevolveNuloNoGetter() {
        Usuario usuario = new Usuario("aluno1", "123456");
        assertNull(usuario.getEndereco());
    }

    @Test
    void getEnderecoDevolveOObjetoDefinidoPeloSetter() {
        Usuario usuario = new Usuario("aluno1", "123456");
        Endereco endereco = new Endereco("Rua Fulano", "100", "Centro",
                "Vitoria da Conquista", "BA", "45000-000");

        usuario.setEndereco(endereco);

        assertSame(endereco, usuario.getEndereco());
        assertEquals("Vitoria da Conquista", usuario.getEndereco().getCidade());
    }

    @Test
    void usuarioInativoNaoConsegueAutenticar() {
        Usuario usuario = new Usuario("aluno1", "123456");
        usuario.setStatus(StatusUsuario.INATIVO);

        assertFalse(usuario.autenticar("aluno1", "123456"));
    }

    @Test
    void usuarioAtivoComCredenciaisCorretasAutentica() {
        Usuario usuario = new Usuario("aluno1", "123456");
        assertTrue(usuario.autenticar("aluno1", "123456"));
    }
}
