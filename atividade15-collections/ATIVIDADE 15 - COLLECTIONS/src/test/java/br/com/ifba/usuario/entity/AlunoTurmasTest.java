package br.com.ifba.usuario.entity;

import br.com.ifba.academico.entity.Curso;
import br.com.ifba.academico.entity.Disciplina;
import br.com.ifba.academico.entity.Turma;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Atividade 11 - Task 05: testes da relação 1..N entre Aluno e Turma.
 */
class AlunoTurmasTest {

    private Aluno aluno;
    private Turma turma;

    @BeforeEach
    void setUp() {
        Curso curso = new Curso("ADS", "Analise e Desenvolvimento de Sistemas", 3200);
        aluno = new Aluno("Maria Silva", "12345678901", "Feminino", "2001-05-10",
                "77999990000", "maria@ifba.edu.br", "maria.silva", "senhaForte1",
                "2024123456", curso);

        Disciplina disciplina = new Disciplina("PROG101", "Programacao I", "Logica e estruturas basicas");
        turma = new Turma("ADS-PROG101-2026.2", disciplina, null, "2026.2");
    }

    @Test
    void alunoRecemCriadoNaoTemTurmas() {
        assertTrue(aluno.getTurmas().isEmpty());
    }

    @Test
    void adicionarTurmaFazAListaCrescer() {
        aluno.adicionarTurma(turma);
        assertEquals(1, aluno.getTurmas().size());
    }

    @Test
    void getTurmasDevolveATurmaRelacionada() {
        aluno.adicionarTurma(turma);
        Turma obtida = aluno.getTurmas().get(0);
        assertSame(turma, obtida);
    }

    @Test
    void listaDevolvidaPeloGetterNaoPodeSerAlteradaPorFora() {
        List<Turma> turmas = aluno.getTurmas();
        assertThrows(UnsupportedOperationException.class,
                () -> turmas.add(turma));
    }
}
