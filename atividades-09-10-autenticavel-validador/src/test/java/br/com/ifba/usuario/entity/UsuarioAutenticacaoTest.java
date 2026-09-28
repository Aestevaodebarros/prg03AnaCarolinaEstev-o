package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UsuarioAutenticacaoTest {

    @Test
    void usuarioComumAutenticaPeloTipoGeral() {
        Autenticavel pessoa = new Usuario("ana", "1234");

        assertEquals("Bem-vindo, ana", autenticar(pessoa, "ana", "1234"));
    }

    @Test
    void administradorAutenticaDeFormaDiferentePeloMesmoTipoGeral() {
        Autenticavel pessoa = new Administrador("root", "1234");

        assertEquals("Acesso administrativo liberado: root",
                autenticar(pessoa, "root", "1234"));
    }

    @Test
    void credenciaisInvalidasSaoRecusadasPorImplementacoesDiferentes() {
        Autenticavel usuario = new Usuario("ana", "1234");
        Autenticavel administrador = new Administrador("root", "1234");

        assertEquals("Credenciais invalidas", autenticar(usuario, "ana", "errada"));
        assertEquals("Credenciais administrativas invalidas",
                autenticar(administrador, "root", "errada"));
    }

    private String autenticar(Autenticavel pessoa, String login, String senha) {
        return pessoa.autenticar(login, senha);
    }
}
