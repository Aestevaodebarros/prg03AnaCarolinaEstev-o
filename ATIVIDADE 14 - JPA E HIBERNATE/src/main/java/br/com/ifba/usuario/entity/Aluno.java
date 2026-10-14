package br.com.ifba.usuario.entity;

import br.com.ifba.academico.entity.Curso;
import br.com.ifba.academico.entity.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Especialização de {@link Usuario} que representa um estudante do IFBA.
 *
 * Atividade 11:
 * - Task 01: herda de Usuario.
 * - Task 03: mantém uma relação 1..N com {@link Turma} usando List. A lista
 *   nunca é exposta por fora da classe: só é possível adicionar um item por
 *   {@link #adicionarTurma(Turma)}, e {@link #getTurmas()} devolve uma
 *   cópia imutável.
 */
public class Aluno extends Usuario {

    private String matricula;
    private Curso curso;
    private final List<Turma> turmas = new ArrayList<>();

    public Aluno(String nome, String cpf, String genero, String dataNascimento,
                 String telefone, String email, String login, String senha,
                 String matricula, Curso curso) {
        super(nome, cpf, genero, dataNascimento, telefone, email, login, senha);
        this.matricula = matricula;
        this.curso = curso;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    /**
     * Único ponto de entrada para vincular uma turma a este aluno.
     */
    public void adicionarTurma(Turma turma) {
        Objects.requireNonNull(turma, "turma não pode ser nula");
        turmas.add(turma);
    }

    /**
     * Devolve uma cópia imutável das turmas do aluno — quem chama este
     * getter não consegue alterar a lista interna por fora da classe.
     */
    public List<Turma> getTurmas() {
        return List.copyOf(turmas);
    }

    /**
     * Atividade 12 - Task 02 (método SOBRESCRITO): descrição de perfil
     * específica do Aluno — matrícula e curso, informações que Professor
     * não tem.
     */
    @Override
    public String descreverPerfil() {
        String nomeCurso = (curso != null) ? curso.getNome() : "sem curso definido";
        return "Aluno " + getNome() + " - matricula " + matricula + " - curso: " + nomeCurso;
    }
}
