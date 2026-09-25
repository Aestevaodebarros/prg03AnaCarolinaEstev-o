package br.com.ifba.usuario.entity;

import java.util.Objects;

/**
 * Endereço de um {@link Usuario}.
 *
 * Atividade 11 - Task 02: relacionamento de composição — um Endereco
 * representa o endereço de exatamente um usuário e não faz sentido
 * existir isoladamente no domínio. O atributo continua privado em
 * {@link Usuario}, acessível apenas por getter/setter.
 */
public class Endereco {

    private String logradouro;
    private String numero;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public Endereco(String logradouro, String numero, String bairro,
                     String cidade, String uf, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.uf = uf;
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public String toString() {
        return String.format("%s, %s - %s, %s/%s - CEP %s",
                logradouro, numero, bairro, cidade, uf, cep);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Endereco)) {
            return false;
        }
        Endereco endereco = (Endereco) o;
        return Objects.equals(logradouro, endereco.logradouro)
                && Objects.equals(numero, endereco.numero)
                && Objects.equals(bairro, endereco.bairro)
                && Objects.equals(cidade, endereco.cidade)
                && Objects.equals(uf, endereco.uf)
                && Objects.equals(cep, endereco.cep);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logradouro, numero, bairro, cidade, uf, cep);
    }
}
