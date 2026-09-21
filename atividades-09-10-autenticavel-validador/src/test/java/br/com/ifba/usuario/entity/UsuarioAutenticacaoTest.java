package br.com.ifba.usuario.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsuarioAutenticacaoTest {

    @Test
    void autenticar_deveRetornarTrue_quandoCredenciaisEstaoCorretas() {
        Usuario usuario = new Usuario("anacarolina", "senhaSegura1");

        assertTrue(usuario.autenticar("anacarolina", "senhaSegura1"));
    }

    @Test
    void autenticar_deveRetornarFalse_quandoSenhaEstaIncorreta() {
        Usuario usuario = new Usuario("anacarolina", "senhaSegura1");

        assertFalse(usuario.autenticar("anacarolina", "senhaErrada"));
    }
}
