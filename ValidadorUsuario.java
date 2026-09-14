package br.com.ifba.usuario.validar;

/**
 * Classe utilitaria de validacao do cadastro de usuario.
 */
public class ValidadorUsuario {

    /**
     * Verifica se o texto informado contem alguma palavra proibida.
     * Metodo estatico: pode ser chamado sem instanciar a classe,
     * ex.: ValidadorUsuario.contemPalavraProibida(login)
     */
    public static boolean contemPalavraProibida(String texto) {

        // array local de palavras proibidas
        String[] palavrasProibidas = {"admin", "teste", "root", "senha123"};

        if (texto == null) {
            return false;
        }

        String textoMinusculo = texto.toLowerCase();

        // percorre o array com for-each verificando se o texto contem alguma delas
        for (String palavra : palavrasProibidas) {
            if (textoMinusculo.contains(palavra)) {
                return true;
            }
        }

        return false;
    }
}
