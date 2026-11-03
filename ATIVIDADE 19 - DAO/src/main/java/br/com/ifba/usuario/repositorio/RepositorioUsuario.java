package br.com.ifba.usuario.repositorio;

import br.com.ifba.infrastructure.dao.GenericDaoImpl;
import br.com.ifba.infrastructure.util.JPAUtil;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.exception.UsuarioNaoEncontradoException;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Atividade 17 - "CRUD completo com tratamento de erros".
 *
 * Substitui o {@code RepositorioUsuarioEmMemoria} da Atividade 15: a
 * assinatura pública (cadastrar/listarTodos/buscarPorLogin/remover) é
 * quase a mesma, exatamente como planejado no PR da Atividade 14 — só o
 * INTERIOR mudou, de uma {@code List}/{@code HashMap} em memória para
 * {@link EntityManager} de verdade.
 *
 * As operações CRUD comuns são delegadas a {@link GenericDaoImpl}, que
 * concentra o controle de transação, rollback e fechamento do
 * {@link EntityManager}. Este repositório mantém as consultas específicas
 * do domínio de usuários.
 */
public class RepositorioUsuario extends GenericDaoImpl<Usuario> {

    public RepositorioUsuario() {
        super(Usuario.class);
    }

    /**
    * Cadastra um usuário usando a operação genérica de persistência.
     */
    public void cadastrar(Usuario usuario) {
        save(usuario);
    }

    /**
    * Edita o usuário com a operação genérica de atualização —
     * o Usuario passado pode ter vindo de uma consulta de uma EntityManager
     * já fechada (TelaEditarUsuario busca o usuário, o usuário sai da tela,
     * os campos são alterados pelos setters, e só então chega aqui), então
     * merge (não persist) é o certo: ele localiza a linha pelo id e
     * sincroniza os campos, em vez de tentar inserir uma linha nova.
     */
    public void editar(Usuario usuario) {
        update(usuario);
    }

    /**
    * Remove o usuário com a operação genérica de exclusão. A implementação
    * faz merge primeiro porque o Usuario vindo da listagem é "detached"
    * (pertence a uma EntityManager já fechada) — remove só aceita uma
    * entidade gerenciada pela EntityManager atual.
     */
    public void remover(Usuario usuario) {
        delete(usuario);
    }

    /**
    * Lista os usuários do banco pela operação genérica de consulta.
     */
    public List<Usuario> listarTodos() {
        return findAll();
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
