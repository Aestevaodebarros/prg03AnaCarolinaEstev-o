package br.com.ifba.usuario.validar;

/**
 * Concentra as regras de validacao do dominio "usuario". As telas (views)
 * devem apenas chamar estes metodos estaticos e exibir a mensagem
 * correspondente; nenhuma regra de negocio deve ficar dentro dos listeners
 * dos botoes.
 */
public class ValidadorUsuario {

    private static final int TAMANHO_CPF = 11;
    private static final int TAMANHO_MINIMO_SENHA = 6;

    /**
     * Verifica se o login contem alguma palavra nao permitida.
     */
    public static boolean contemPalavraProibida(String login) {
        String[] palavrasProibidas = {"admin", "teste", "root", "senha123"};
        if (login == null) {
            return false;
        }
        String loginMinusculo = login.toLowerCase();
        for (String palavra : palavrasProibidas) {
            if (loginMinusculo.contains(palavra)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se nenhum dos campos informados esta nulo, vazio ou
     * composto apenas por espacos em branco.
     */
    public static boolean camposPreenchidos(String... campos) {
        if (campos == null || campos.length == 0) {
            return false;
        }
        for (String campo : campos) {
            if (campo == null || campo.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Verifica se o CPF informado e valido: exatamente 11 digitos numericos
     * (pontos, hifen e espacos sao ignorados na contagem) e nao formado
     * apenas por digitos repetidos (ex.: 00000000000).
     */
    public static boolean cpfValido(String cpf) {
        if (cpf == null) {
            return false;
        }
        String cpfLimpo = cpf.replaceAll("[.\\-\\s]", "");
        if (cpfLimpo.length() != TAMANHO_CPF) {
            return false;
        }
        for (int i = 0; i < cpfLimpo.length(); i++) {
            if (!Character.isDigit(cpfLimpo.charAt(i))) {
                return false;
            }
        }
        boolean todosIguais = true;
        for (int i = 1; i < cpfLimpo.length(); i++) {
            if (cpfLimpo.charAt(i) != cpfLimpo.charAt(0)) {
                todosIguais = false;
                break;
            }
        }
        return !todosIguais;
    }

    /**
     * Verifica se a senha atinge o tamanho minimo exigido e mistura letras
     * e numeros.
     */
    public static boolean senhaForte(String senha) {
        if (senha == null || senha.length() < TAMANHO_MINIMO_SENHA) {
            return false;
        }
        boolean temLetra = false;
        boolean temNumero = false;
        for (int i = 0; i < senha.length(); i++) {
            char c = senha.charAt(i);
            if (Character.isLetter(c)) {
                temLetra = true;
            } else if (Character.isDigit(c)) {
                temNumero = true;
            }
        }
        return temLetra && temNumero;
    }

    /**
     * Verifica se a senha e a confirmacao informadas sao iguais.
     */
    public static boolean senhasConferem(String senha, String confirmarSenha) {
        return senha != null && senha.equals(confirmarSenha);
    }
}
