package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * Atividade 13 - Task 05 ("um teste por forma"): confirma que
 * {@link Usuario} e {@link Administrador} respondem de jeitos diferentes
 * ao MESMO método {@code autenticar(...)}, sempre chamado através do tipo
 * geral {@link Autenticavel} — sem nenhum if, instanceof ou comparação de
 * tipo em nenhum lugar (nem aqui no teste, nem no método de produção).
 */
class PolimorfismoAutenticacaoTest {

    @Test
    void usuarioComumAutenticaPeloProprioCanal() {
        Autenticavel pessoa = new Usuario("ana", "1234"); // tipo geral à esquerda
        assertEquals("Bem-vindo, ana", pessoa.autenticar("ana", "1234"));
    }

    @Test
    void administradorAutenticaDeFormaDiferente() {
        Autenticavel pessoa = new Administrador("root", "1234");
        assertEquals("Acesso administrativo liberado: root", pessoa.autenticar("root", "1234"));
    }

    @Test
    void usuarioComumComCredenciaisErradasNaoAutentica() {
        Autenticavel pessoa = new Usuario("ana", "1234");
        assertEquals("Acesso negado", pessoa.autenticar("ana", "senhaErrada"));
    }

    @Test
    void administradorComCredenciaisErradasNaoAutentica() {
        Autenticavel pessoa = new Administrador("root", "1234");
        assertEquals("Acesso administrativo negado", pessoa.autenticar("root", "senhaErrada"));
    }

    /**
     * Task 03: um único método estático (Autenticavel.processar), sempre
     * recebendo o TIPO GERAL. Chamado duas vezes, com um objeto de cada
     * classe — as saídas são diferentes, e processar() nunca soube qual
     * classe concreta recebeu.
     */
    @Test
    void processarFuncionaParaQualquerAutenticavelSemSaberQualClasseE() {
        Autenticavel usuarioComum = new Usuario("ana", "1234");
        Autenticavel admin = new Administrador("root", "1234");

        String resultadoUsuario = Autenticavel.processar(usuarioComum, "ana", "1234");
        String resultadoAdmin = Autenticavel.processar(admin, "root", "1234");

        assertEquals("Bem-vindo, ana", resultadoUsuario);
        assertEquals("Acesso administrativo liberado: root", resultadoAdmin);
        assertNotEquals(resultadoUsuario, resultadoAdmin);
    }

    /**
     * Aluno e Professor não sobrescrevem autenticar(...) (ele continua
     * herdado de Usuario, como decidido na Atividade 12) — mas ainda assim
     * respondem corretamente quando tratados pelo tipo geral Autenticavel.
     */
    @Test
    void alunoUsaAutenticarHerdadoDeUsuarioAtravesDoTipoGeral() {
        Aluno aluno = new Aluno("Maria Silva", "12345678901", "Feminino", "2001-05-10",
                "77999990000", "maria@ifba.edu.br", "maria.silva", "senhaForte1",
                "2024123456", null);

        Autenticavel pessoa = aluno;

        assertEquals("Bem-vindo, maria.silva", pessoa.autenticar("maria.silva", "senhaForte1"));
    }
}
