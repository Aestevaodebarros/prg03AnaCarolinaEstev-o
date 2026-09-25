package br.com.ifba.usuario.entity;

import br.com.ifba.academico.entity.Curso;
import br.com.ifba.academico.entity.Disciplina;
import br.com.ifba.academico.entity.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Atividade 12 - Task 02 ("mantenha a rede"): garante que o comportamento
 * HERDADO ({@link Usuario#saudacao()}) continua igual para qualquer
 * subclasse, e que o comportamento SOBRESCRITO
 * ({@link Usuario#descreverPerfil()}) devolve o resultado específico de
 * cada filha ({@link Aluno} e {@link Professor}).
 */
class UsuarioHerancaTest {

    private Aluno aluno;
    private Professor professor;

    @BeforeEach
    void setUp() {
        Curso curso = new Curso("ADS", "Analise e Desenvolvimento de Sistemas", 3200);
        aluno = new Aluno("Maria Silva", "12345678901", "Feminino", "2001-05-10",
                "77999990000", "maria@ifba.edu.br", "maria.silva", "senhaForte1",
                "2024123456", curso);

        professor = new Professor("Joao Souza", "10987654321", "Masculino", "1980-02-20",
                "77988887777", "joao.souza@ifba.edu.br", "joao.souza", "senhaForte2",
                "SIAPE-1122334");
    }

    // ---- comportamento HERDADO: nenhuma subclasse sobrescreve saudacao() ----

    @Test
    void alunoUsaSaudacaoHerdadaDeUsuarioSemAlteracao() {
        assertEquals("Bem-vindo(a), Maria Silva!", aluno.saudacao());
    }

    @Test
    void professorUsaSaudacaoHerdadaDeUsuarioSemAlteracao() {
        assertEquals("Bem-vindo(a), Joao Souza!", professor.saudacao());
    }

    @Test
    void alunoESaudacaoDoUsuarioGenericoTemOMesmoFormato() {
        Usuario usuarioGenerico = new Usuario("Maria Silva", "12345678901", "Feminino",
                "2001-05-10", "77999990000", "maria@ifba.edu.br", "maria.silva", "senhaForte1");

        // mesmo texto para o usuário genérico e para o Aluno: prova que
        // Aluno não reescreveu saudacao(), apenas herdou de Usuario.
        assertEquals(usuarioGenerico.saudacao(), aluno.saudacao());
    }

    // ---- comportamento SOBRESCRITO: cada filha devolve seu próprio resultado ----

    @Test
    void alunoSobrescreveDescreverPerfilComMatriculaECurso() {
        String perfil = aluno.descreverPerfil();
        assertTrue(perfil.contains("Aluno Maria Silva"));
        assertTrue(perfil.contains("2024123456"));
        assertTrue(perfil.contains("Analise e Desenvolvimento de Sistemas"));
    }

    @Test
    void professorSobrescreveDescreverPerfilComSiape() {
        String perfil = professor.descreverPerfil();
        assertTrue(perfil.contains("Professor Joao Souza"));
        assertTrue(perfil.contains("SIAPE-1122334"));
    }

    @Test
    void alunoEProfessorDevolvemPerfisDiferentesParaOMesmoMetodo() {
        // mesma chamada de método (polimorfismo), resultados diferentes
        assertTrue(!aluno.descreverPerfil().equals(professor.descreverPerfil()));
    }

    @Test
    void professorComTurmaReflenteAQuantidadeNoPerfilSobrescrito() {
        Disciplina disciplina = new Disciplina("PROG101", "Programacao I", "Logica e estruturas basicas");
        Turma turma = new Turma("ADS-PROG101-2026.2", disciplina, professor, "2026.2");

        professor.adicionarTurma(turma);

        assertTrue(professor.descreverPerfil().contains("1 turma(s)"));
    }
}
