package br.com.ifba.academico.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Curso oferecido pelo IFBA (ex.: Análise e Desenvolvimento de Sistemas).
 *
 * Atividade 11 - Task 03: mantém a matriz curricular como uma relação
 * 1..N com {@link Disciplina} usando List. A lista só cresce por
 * {@link #adicionarDisciplina(Disciplina)}; {@link #getDisciplinas()}
 * devolve uma cópia imutável, nunca a referência interna.
 */
public class Curso {

    private String codigo;
    private String nome;
    private int cargaHoraria;
    private final List<Disciplina> disciplinas = new ArrayList<>();

    public Curso(String codigo, String nome, int cargaHoraria) {
        this.codigo = codigo;
        this.nome = nome;
        this.cargaHoraria = cargaHoraria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public void adicionarDisciplina(Disciplina disciplina) {
        Objects.requireNonNull(disciplina, "disciplina não pode ser nula");
        disciplinas.add(disciplina);
    }

    public List<Disciplina> getDisciplinas() {
        return List.copyOf(disciplinas);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Curso)) {
            return false;
        }
        Curso curso = (Curso) o;
        return Objects.equals(codigo, curso.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
