package br.com.ifba.academico.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Atividade 11 - Task 05: testes da relação 1..N entre Curso e Disciplina.
 */
class CursoTest {

    private Curso curso;

    @BeforeEach
    void setUp() {
        curso = new Curso("ADS", "Analise e Desenvolvimento de Sistemas", 3200);
    }

    @Test
    void cursoRecemCriadoNaoTemDisciplinas() {
        assertTrue(curso.getDisciplinas().isEmpty());
    }

    @Test
    void adicionarDisciplinaFazAListaCrescer() {
        curso.adicionarDisciplina(new Disciplina("PROG101", "Programacao I", "Logica e estruturas basicas"));
        assertEquals(1, curso.getDisciplinas().size());

        curso.adicionarDisciplina(new Disciplina("PROG102", "Programacao II", "POO"));
        assertEquals(2, curso.getDisciplinas().size());
    }

    @Test
    void getDisciplinasDevolveOObjetoRelacionado() {
        Disciplina programacao = new Disciplina("PROG101", "Programacao I", "Logica e estruturas basicas");
        curso.adicionarDisciplina(programacao);

        Disciplina obtida = curso.getDisciplinas().get(0);
        assertEquals("Programacao I", obtida.getNome());
        assertSame(programacao, obtida);
    }

    @Test
    void listaDevolvidaPeloGetterNaoPodeSerAlteradaPorFora() {
        List<Disciplina> disciplinas = curso.getDisciplinas();
        assertThrows(UnsupportedOperationException.class,
                () -> disciplinas.add(new Disciplina("X", "Invasora", "")));
    }
}
