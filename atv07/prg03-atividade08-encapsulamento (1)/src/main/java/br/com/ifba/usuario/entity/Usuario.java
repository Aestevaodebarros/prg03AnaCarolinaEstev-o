package br.com.ifba.usuario.entity;

/**
 * Classe de dominio Usuario.
 *
 * Atividade 08 - Encapsulamento:
 * Todos os atributos sao privados e so podem ser lidos por fora da classe
 * atraves dos metodos getters. A criacao de um Usuario "completo" (usado na
 * Tela de Cadastro) deve ser feita pelo construtor com parametros.
 */
public class Usuario {

    private String nome;
    private String cpf;
    private String genero;
    private String dataNascimento;
    private String telefone;
    private String email;
    private String login;
    private String senha;

    /**
     * Construtor com parametros, usado no cadastro completo de um usuario
     * (Tela de Cadastro de Usuario).
     */
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

    /**
     * Construtor simplificado, usado na Tela de Login, onde apenas
     * login e senha sao digitados.
     */
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
}
