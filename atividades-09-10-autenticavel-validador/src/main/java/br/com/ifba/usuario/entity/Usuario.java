package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;

/**
 * Classe de dominio que representa um usuario do Sistema Academico.
 */
public class Usuario implements Autenticavel {

    private String nome;
    private String cpf;
    private String genero;
    private String dataNascimento;
    private String telefone;
    private String email;
    private String login;
    private String senha;

    public Usuario(String nome, String cpf, String genero, String dataNascimento,
                    String telefone, String email, String login, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.login = login;
        this.senha = senha;
    }

    public Usuario(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getGenero() {
        return genero;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    /**
     * Implementacao de Autenticavel. A comparacao e feita inteiramente
     * dentro da classe, usando this.login e this.senha (que continuam
     * private): nenhum codigo externo precisa ler esses atributos para
     * saber se o acesso deve ser liberado.
     */
    @Override
    public boolean autenticar(String login, String senha) {
        if (login == null || senha == null) {
            return false;
        }
        return login.equals(this.login) && senha.equals(this.senha);
    }
}
