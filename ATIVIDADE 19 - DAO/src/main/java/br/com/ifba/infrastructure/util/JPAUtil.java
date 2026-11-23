package br.com.ifba.infrastructure.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
            Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, carregarConfiguracaoJpa());

    private JPAUtil() {
        // classe utilitária: só métodos estáticos, não deve ser instanciada
    }

    private static Map<String, String> carregarConfiguracaoJpa() {
        Properties properties = new Properties();
        try (InputStream input = JPAUtil.class.getClassLoader()
                .getResourceAsStream("application.properties") == null
                ? null
                : JPAUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Nao foi possivel carregar application.properties", exception);
        }

        Map<String, String> jpaProperties = new HashMap<>();
        adicionarPropriedade(jpaProperties, properties,
                "spring.datasource.driver-class-name", "javax.persistence.jdbc.driver");
        adicionarPropriedade(jpaProperties, properties,
                "spring.datasource.url", "javax.persistence.jdbc.url");
        adicionarPropriedade(jpaProperties, properties,
                "spring.datasource.username", "javax.persistence.jdbc.user");
        adicionarPropriedade(jpaProperties, properties,
                "spring.datasource.password", "javax.persistence.jdbc.password");
        adicionarPropriedade(jpaProperties, properties,
                "spring.jpa.database-platform", "hibernate.dialect");
        adicionarPropriedade(jpaProperties, properties,
                "spring.jpa.hibernate.ddl-auto", "hibernate.hbm2ddl.auto");
        return jpaProperties;
    }

    private static void adicionarPropriedade(Map<String, String> destino,
                                             Properties origem,
                                             String origemNome,
                                             String destinoNome) {
        String value = origem.getProperty(origemNome);
        if (value != null) {
            destino.put(destinoNome, value);
        }
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
