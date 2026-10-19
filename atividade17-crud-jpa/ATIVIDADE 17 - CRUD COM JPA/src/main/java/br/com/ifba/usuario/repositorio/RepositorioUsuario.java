package br.com.ifba.usuario.repositorio;

import br.com.ifba.infrastructure.util.JPAUtil;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.exception.UsuarioNaoEncontradoException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;

/**
 * Atividade 17 - "CRUD completo com tratamento de erros".
 *
 * Substitui o {@code RepositorioUsuarioEmMemoria} da Atividade 15: a
 * assinatura pública (cadastrar/listarTodos/buscarPorLogin/remover) é
 * quase a mesma, exatamente como planejado no PR da Atividade 14 — só o
 * INTERIOR mudou, de uma {@code List}/{@code HashMap} em memória para
 * {@link EntityManager} de verdade.
 *
 * Padrão repetido em todo método de escrita (Task 02/Task 06):
 * <pre>
 * EntityManager em = JPAUtil.getEntityManager();
 * try {
 *     em.getTransaction().begin();
 *     ... persist / merge / remove ...
 *     em.getTransaction().commit();
 * } catch (RuntimeException e) {
 *     if (em.getTransaction().isActive()) {
 *         em.getTransaction().rollback();
 *     }
 *     throw e;
 * } finally {
 *     em.close();
 * }
 * </pre>
 * O {@code finally} garante que o EntityManager é sempre fechado, dê certo
 * ou não. O rollback só acontece se a transação ainda estiver ativa
 * (evita {@code IllegalStateException} tentando dar rollback numa
 * transação que nunca começou ou que já foi commitada). Nenhum catch
 * aqui fica vazio nem só imprime o stack trace — a exceção sempre sobe
 * para quem chamou decidir o que mostrar na tela (Task 05).
 */
public class RepositorioUsuario {

    /**
     * Task 01: cadastra gravando de verdade no banco, via
     * {@link EntityManager#persist(Object)}, dentro de uma transação.
     */
    public void cadastrar(Usuario usuario) {
        Objects.requireNonNull(usuario, "usuario não pode ser nulo");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Task 02: edita atualizando com {@link EntityManager#merge(Object)} —
     * o Usuario passado pode ter vindo de uma consulta de uma EntityManager
     * já fechada (TelaEditarUsuario busca o usuário, o usuário sai da tela,
     * os campos são alterados pelos setters, e só então chega aqui), então
     * merge (não persist) é o certo: ele localiza a linha pelo id e
     * sincroniza os campos, em vez de tentar inserir uma linha nova.
     */
    public void editar(Usuario usuario) {
        Objects.requireNonNull(usuario, "usuario não pode ser nulo");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(usuario);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Task 02: remove com {@link EntityManager#remove(Object)}. merge
     * primeiro porque o Usuario vindo da tabela da listagem é "detached"
     * (pertence a uma EntityManager já fechada) — remove só aceita uma
     * entidade gerenciada pela EntityManager atual.
     */
    public void remover(Usuario usuario) {
        Objects.requireNonNull(usuario, "usuario não pode ser nulo");
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Usuario gerenciado = em.merge(usuario);
            em.remove(gerenciado);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Task 02: lista trazendo do banco com JPQL, não mais de uma List em
     * memória. Leitura não abre transação (não escreve nada).
     */
    public List<Usuario> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u", Usuario.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca por login (ex.: checar duplicidade antes de cadastrar, ou
     * autenticar no login). {@code null} aqui é uma resposta válida e
     * esperada ("esse login não existe ainda") — não é o caso de uso da
     * Task 04/exceção, porque todo chamador deste método já checa o
     * retorno null de propósito.
     */
    public Usuario buscarPorLogin(String login) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE LOWER(u.login) = LOWER(:login)", Usuario.class);
            query.setParameter("login", login);
            List<Usuario> resultado = query.getResultList();
            return resultado.isEmpty() ? null : resultado.get(0);
        } finally {
            em.close();
        }
    }

    /**
     * Atividade 17 - Task 04: versão "ou falha" de
     * {@link #buscarPorLogin(String)}.
     *
     * Usada por {@code TelaEditarUsuario} (Task 02) para buscar de novo,
     * no banco, o usuário que o operador clicou em "Editar" na listagem.
     *
     * ANTES (Atividade 14, repositório em memória): se o usuário
     * selecionado tivesse sido removido por outra tela entre o momento em
     * que a listagem foi carregada e o clique em "Editar", o método de
     * busca devolvia {@code null} silenciosamente. Se o código de
     * TelaEditarUsuario não checasse esse null explicitamente (fácil de
     * esquecer — o compilador não obriga), a primeira linha que chamasse
     * {@code usuario.getNome()} para preencher um campo da tela explodiria
     * com {@link NullPointerException}, numa pilha de erro que não diz
     * nada sobre "o usuário foi removido" — só "algo é null em algum
     * lugar".
     *
     * AGORA: o problema é detectado exatamente aqui, no ponto em que ele
     * acontece (não três chamadas de método depois), com uma mensagem que
     * já explica a causa provável. {@code TelaEditarUsuario} captura esta
     * exceção (Task 05) e mostra a mensagem num JOptionPane, em vez de a
     * aplicação travar.
     */
    public Usuario buscarPorLoginOuFalhar(String login) {
        Usuario usuario = buscarPorLogin(login);
        if (usuario == null) {
            throw new UsuarioNaoEncontradoException(login);
        }
        return usuario;
    }

    /**
     * Atividade 17 - Task 07: busca textual via JPQL com WHERE, usada pelo
     * campo de busca de {@code TelaListarUsuarios}. Substitui o filtro em
     * memória (Atividade 14) por uma consulta real ao banco.
     */
    public List<Usuario> buscarPorTermo(String termo) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Usuario> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE LOWER(u.login) LIKE :termo OR LOWER(u.nome) LIKE :termo",
                    Usuario.class);
            query.setParameter("termo", "%" + termo.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
