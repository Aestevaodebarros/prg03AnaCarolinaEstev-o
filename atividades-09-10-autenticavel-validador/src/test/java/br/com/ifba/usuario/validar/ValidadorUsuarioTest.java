package br.com.ifba.usuario.validar;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorUsuarioTest {

    // ---------- camposPreenchidos ----------

    @Test
    void camposPreenchidos_caminhoFeliz_todosOsCamposPreenchidos() {
        assertTrue(ValidadorUsuario.camposPreenchidos("Ana", "12345678901", "11999999999"));
    }

    @Test
    void camposPreenchidos_deveFalhar_quandoUmCampoEstaVazio() {
        assertFalse(ValidadorUsuario.camposPreenchidos("Ana", "", "11999999999"));
    }

    @Test
    void camposPreenchidos_deveFalhar_quandoUmCampoENulo() {
        assertFalse(ValidadorUsuario.camposPreenchidos("Ana", null, "11999999999"));
    }

    @Test
    void camposPreenchidos_deveFalhar_quandoCampoTemApenasEspacos() {
        assertFalse(ValidadorUsuario.camposPreenchidos("Ana", "   "));
    }

    // ---------- cpfValido ----------

    @Test
    void cpfValido_caminhoFeliz_onzeDigitosNumericos() {
        assertTrue(ValidadorUsuario.cpfValido("12345678901"));
    }

    @Test
    void cpfValido_caminhoFeliz_cpfFormatadoComPontosEHifen() {
        assertTrue(ValidadorUsuario.cpfValido("123.456.789-01"));
    }

    @Test
    void cpfValido_deveFalhar_paraCpfVazio() {
        assertFalse(ValidadorUsuario.cpfValido(""));
    }

    @Test
    void cpfValido_deveFalhar_paraCpfNulo() {
        assertFalse(ValidadorUsuario.cpfValido(null));
    }

    @Test
    void cpfValido_deveFalhar_paraCpfComLetras() {
        assertFalse(ValidadorUsuario.cpfValido("123abc78901"));
    }

    @Test
    void cpfValido_deveFalhar_paraCpfComMenosDeOnzeDigitos() {
        assertFalse(ValidadorUsuario.cpfValido("123456789"));
    }

    @Test
    void cpfValido_deveFalhar_paraCpfComTodosOsDigitosIguais() {
        assertFalse(ValidadorUsuario.cpfValido("00000000000"));
    }

    // ---------- senhaForte ----------

    @Test
    void senhaForte_caminhoFeliz_senhaComLetrasENumerosNoTamanhoMinimo() {
        assertTrue(ValidadorUsuario.senhaForte("abc123"));
    }

    @Test
    void senhaForte_deveFalhar_paraSenhaComMenosCaracteresQueOMinimo() {
        assertFalse(ValidadorUsuario.senhaForte("ab1"));
    }

    @Test
    void senhaForte_deveFalhar_paraSenhaNula() {
        assertFalse(ValidadorUsuario.senhaForte(null));
    }

    @Test
    void senhaForte_deveFalhar_paraSenhaSoComLetras() {
        assertFalse(ValidadorUsuario.senhaForte("abcdef"));
    }

    @Test
    void senhaForte_deveFalhar_paraSenhaSoComNumeros() {
        assertFalse(ValidadorUsuario.senhaForte("123456"));
    }

    // ---------- senhasConferem ----------

    @Test
    void senhasConferem_caminhoFeliz_senhasIguais() {
        assertTrue(ValidadorUsuario.senhasConferem("abc123", "abc123"));
    }

    @Test
    void senhasConferem_deveFalhar_quandoSenhasDiferentes() {
        assertFalse(ValidadorUsuario.senhasConferem("abc123", "abc124"));
    }

    // ---------- contemPalavraProibida (regressao) ----------

    @Test
    void contemPalavraProibida_deveDetectarPalavraProibidaNoLogin() {
        assertTrue(ValidadorUsuario.contemPalavraProibida("admin123"));
    }

    @Test
    void contemPalavraProibida_deveAceitarLoginPermitido() {
        assertFalse(ValidadorUsuario.contemPalavraProibida("anacarolina"));
    }
}
