package br.com.ifba.usuario.interfaces;

/**
 * Contrato para qualquer entidade que possa ser autenticada no sistema
 * a partir de um login e uma senha.
 */
public interface Autenticavel {

    /**
     * Verifica se o login e a senha informados conferem com os dados
     * armazenados internamente pela entidade que implementa esta interface.
     *
     * @param login login informado
     * @param senha senha informada
     * @return true se as credenciais forem validas, false caso contrario
     */
    boolean autenticar(String login, String senha);
}
