package br.com.ifba.academico.entity;

import java.util.Objects;

/**
 * Disciplina de uma matriz curricular (ex.: "Programação I").
 */
public class Disciplina {

    private String codigo;
    private String nome;
    private String ementa;

    public Disciplina(String codigo, String nome, String ementa) {
        this.codigo = codigo;
        this.nome = nome;
        this.ementa = ementa;
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

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", codigo, nome);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Disciplina)) {
            return false;
        }
        Disciplina disciplina = (Disciplina) o;
        return Objects.equals(codigo, disciplina.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
