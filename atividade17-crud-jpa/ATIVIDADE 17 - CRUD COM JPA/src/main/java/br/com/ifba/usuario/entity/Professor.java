package br.com.ifba.usuario.entity;

import br.com.ifba.academico.entity.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Especialização de {@link Usuario} que representa um professor do IFBA.
 *
 * Atividade 11:
 * - Task 01: herda de Usuario.
 * - Task 03: mantém a relação 1..N com {@link Turma} da mesma forma que
 *   {@link Aluno} — adição controlada, getter devolve cópia imutável.
 */
public class Professor extends Usuario {

    private String siape;
    private final List<Turma> turmas = new ArrayList<>();

    public Professor(String nome, String cpf, String genero, String dataNascimento,
                      String telefone, String email, String login, String senha,
                      String siape) {
        super(nome, cpf, genero, dataNascimento, telefone, email, login, senha);
        this.siape = siape;
    }

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public void adicionarTurma(Turma turma) {
        Objects.requireNonNull(turma, "turma não pode ser nula");
        turmas.add(turma);
    }

    public List<Turma> getTurmas() {
        return List.copyOf(turmas);
    }

    /**
     * Atividade 12 - Task 02 (método SOBRESCRITO): descrição de perfil
     * específica do Professor — SIAPE e quantidade de turmas, informação
     * que Aluno não tem.
     */
    @Override
    public String descreverPerfil() {
        return "Professor " + getNome() + " - SIAPE " + siape
                + " - " + turmas.size() + " turma(s)";
    }
}
