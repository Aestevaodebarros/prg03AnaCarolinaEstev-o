package br.com.ifba.academico.entity;

import br.com.ifba.usuario.entity.Professor;

import java.util.Objects;

/**
 * Turma: uma oferta concreta de uma {@link Disciplina} em um período letivo,
 * associada a um {@link Professor} responsável.
 *
 * {@link br.com.ifba.usuario.entity.Aluno} e {@link Professor} guardam suas
 * turmas em uma List (Atividade 11 - Task 03); a Turma em si só
 * referencia disciplina e professor por associação simples, sem "possuir"
 * nenhum dos dois.
 */
public class Turma {

    private String codigo;
    private Disciplina disciplina;
    private Professor professor;
    private String periodo; // ex.: "2026.2"

    public Turma(String codigo, Disciplina disciplina, Professor professor, String periodo) {
        this.codigo = codigo;
        this.disciplina = Objects.requireNonNull(disciplina, "disciplina não pode ser nula");
        this.professor = professor;
        this.periodo = periodo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = Objects.requireNonNull(disciplina, "disciplina não pode ser nula");
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        return String.format("%s - %s (%s)", codigo, disciplina.getNome(), periodo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Turma)) {
            return false;
        }
        Turma turma = (Turma) o;
        return Objects.equals(codigo, turma.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
