package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;

import java.util.Objects;

/**
 * Administrador do sistema.
 *
 * Atividade 13 - Task 01: esta é a "segunda forma" do método
 * {@code autenticar(...)}. Administrador implementa {@link Autenticavel}
 * diretamente — não é um {@link Usuario}, não herda nada dele, não tem
 * matrícula nem SIAPE. É uma classe completamente separada da hierarquia
 * de Usuario/Aluno/Professor, ligada a ela só pelo contrato em comum.
 *
 * Isso prova que o polimorfismo aqui vem da INTERFACE (qualquer classe que
 * implemente Autenticavel pode ser tratada como Autenticavel), e não de
 * uma superclasse compartilhada.
 */
public class Administrador implements Autenticavel {

    private final String login;
    private final String senha;

    public Administrador(String login, String senha) {
        this.login = Objects.requireNonNull(login, "login não pode ser nulo");
        this.senha = Objects.requireNonNull(senha, "senha não pode ser nula");
    }

    public String getLogin() {
        return login;
    }

    /**
     * Comportamento propositalmente diferente do de {@link Usuario}: a
     * mensagem de sucesso é outra ("Acesso administrativo liberado: ..."),
     * mostrando que cada classe responde do seu próprio jeito ao mesmo
     * método — sem que quem chamou precise saber qual classe é qual.
     */
    @Override
    public String autenticar(String login, String senha) {
        boolean credenciaisConferem = login != null && senha != null
                && login.equals(this.login) && senha.equals(this.senha);
        if (credenciaisConferem) {
            return "Acesso administrativo liberado: " + login;
        }
        return "Acesso administrativo negado";
    }
}
