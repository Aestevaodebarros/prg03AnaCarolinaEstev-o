package br.com.ifba.usuario.interfaces;

/**
 * Contrato para qualquer entidade que possa ser autenticada no sistema
 * a partir de um login e uma senha.
 *
 * Atividade 13 - Task 02 ("faça o método devolver, não imprimir"): o
 * método não imprime nada (nem no console, nem em tela) — ele DEVOLVE uma
 * mensagem. Quem chamou é quem decide o que fazer com o resultado:
 * imprimir, mostrar em um JOptionPane, ou comparar em um teste.
 */
public interface Autenticavel {

    /**
     * Verifica se o login e a senha informados conferem com os dados
     * armazenados internamente pela entidade que implementa esta interface,
     * e devolve uma mensagem descrevendo o resultado da autenticação.
     *
     * O formato exato da mensagem de sucesso é decidido por cada
     * implementação (ex.: {@link br.com.ifba.usuario.entity.Usuario} devolve
     * "Bem-vindo, {login}"; {@link br.com.ifba.usuario.entity.Administrador}
     * devolve "Acesso administrativo liberado: {login}") — é exatamente
     * essa diferença de comportamento para o mesmo método que caracteriza
     * o polimorfismo (Atividade 13).
     *
     * @param login login informado
     * @param senha senha informada
     * @return mensagem de sucesso quando as credenciais são válidas, ou uma
     *         mensagem de acesso negado caso contrário
     */
    String autenticar(String login, String senha);

    /**
     * Atividade 13 - Task 03 ("escreva o método que atende a todas"):
     * recebe sempre o TIPO GERAL (Autenticavel), nunca uma classe
     * concreta. Não existe nenhum if, instanceof ou comparação de tipo
     * aqui dentro — quem decide como se autenticar é sempre o próprio
     * objeto recebido, via polimorfismo.
     */
    static String processar(Autenticavel pessoa, String login, String senha) {
        return pessoa.autenticar(login, senha);
    }
}
