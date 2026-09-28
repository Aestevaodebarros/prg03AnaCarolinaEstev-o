package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;

public class Administrador implements Autenticavel {

    private final String login;
    private final String senha;

    public Administrador(String login, String senha) {
        this.login = login;
        this.senha = senha;
    }

    @Override
    public String autenticar(String login, String senha) {
        if (login != null && senha != null
                && login.equals(this.login) && senha.equals(this.senha)) {
            return "Acesso administrativo liberado: " + this.login;
        }
        return "Credenciais administrativas invalidas";
    }
}