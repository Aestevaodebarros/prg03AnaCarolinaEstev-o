package br.com.ifba.infrastructure.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Atividade 17 - Task 01: fábrica única do EntityManagerFactory.
 *
 * {@link EntityManagerFactory} é cara de criar (lê o persistence.xml,
 * valida o mapeamento de todas as entidades anotadas, abre o pool de
 * conexão) e é pensada para ser criada UMA VEZ por aplicação, não uma vez
 * por tela/operação. Por isso esta classe guarda a fábrica num campo
 * {@code static final}, inicializado no carregamento da classe — nenhuma
 * tela (TelaCadastroUsuario, TelaListarUsuarios, TelaEditarUsuario, ...)
 * chama {@code Persistence.createEntityManagerFactory(...)} por conta
 * própria; todas pedem um {@link EntityManager} aqui.
 *
 * {@link EntityManager}, ao contrário da fábrica, é barato e NÃO é
 * thread-safe / não deve ser reaproveitado entre operações — por isso
 * {@link #getEntityManager()} devolve uma instância nova a cada chamada,
 * e quem pede tem a responsabilidade de fechar (sempre num {@code finally},
 * Task 06).
 */
public final class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "prg03aluno-pu";

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    private JPAUtil() {
        // classe utilitária: só métodos estáticos, não deve ser instanciada
    }

    /**
     * Devolve um EntityManager novo, pronto para uma única operação
     * (um cadastrar/listar/editar/excluir). Quem chama este método é
     * responsável por fechar o EntityManager devolvido (idealmente num
     * bloco finally — Task 06).
     */
    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    /**
     * Encerra a fábrica. Chamado uma única vez, ao fechar a aplicação
     * (ver {@code TelaLogin} - fechar a janela principal libera a conexão
     * com o banco em vez de deixá-la presa até o processo morrer).
     */
    public static void close() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
