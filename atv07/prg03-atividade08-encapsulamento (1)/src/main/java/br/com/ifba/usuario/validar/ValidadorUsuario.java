package br.com.ifba.usuario.validar;

/**
 * Classe utilitaria de validacao do Usuario (Atividade 06).
 */
public class ValidadorUsuario {

    public static boolean contemPalavraProibida(String texto) {
        String[] palavrasProibidas = {"admin", "teste", "root", "senha123"};

        if (texto == null) {
            return false;
        }

        String textoMinusculo = texto.toLowerCase();

        for (String palavra : palavrasProibidas) {
            if (textoMinusculo.contains(palavra)) {
                return true;
            }
        }

        return false;
    }
}
