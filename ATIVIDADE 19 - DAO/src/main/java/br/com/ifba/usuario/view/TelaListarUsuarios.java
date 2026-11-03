package br.com.ifba.usuario.view;

import br.com.ifba.login.view.TelaLogin;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repositorio.RepositorioUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Atividade 14 - Task 04 ("Telas do CRUD"): tela de LISTAGEM do domínio
 * (Usuario). Junto com {@link TelaCadastroUsuario} (cadastro) e
 * {@link TelaEditarUsuario} (edição), fecha o trio de telas pedido pela
 * atividade.
 *
 * Atividade 17 - Task 02/Task 07: agora busca e lista de verdade no banco,
 * via {@link RepositorioUsuario} (JPA/Hibernate, através de
 * {@code JPAUtil}) — nada de lista em memória. O campo de busca usa JPQL
 * com {@code WHERE} ({@link RepositorioUsuario#buscarPorTermo(String)}).
 *
 * Atividade 14 - Task 02/Task 06: a tabela mostra só login, nome, email e
 * status — nunca cpf, dataNascimento ou senha (ver RETROSPECTIVA.md).
 */
public class TelaListarUsuarios extends JFrame {

    private static final String[] COLUNAS = {"Login", "Nome", "Email", "Status"};

    private final RepositorioUsuario repositorio = new RepositorioUsuario();
    private final List<Usuario> usuariosExibidos = new ArrayList<>();

    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaListarUsuarios() {
        montarTela();
        atualizarTabela(null);
    }

    private void montarTela() {
        setTitle("Usuarios Cadastrados");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(640, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // topo: busca + novo usuario (equivalente ao "+" do exemplo da tela)
        JPanel painelTopo = new JPanel(new BorderLayout(8, 0));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        txtBusca = new JTextField();
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

        JPanel painelBusca = new JPanel(new BorderLayout(5, 0));
        painelBusca.add(new JLabel("Buscar:"), BorderLayout.WEST);
        painelBusca.add(txtBusca, BorderLayout.CENTER);
        painelBusca.add(btnBuscar, BorderLayout.EAST);

        JButton btnNovo = new JButton("+ Novo Usuario");
        btnNovo.addActionListener(this::btnNovoActionPerformed);

        painelTopo.add(painelBusca, BorderLayout.CENTER);
        painelTopo.add(btnNovo, BorderLayout.EAST);

        // centro: tabela de usuarios
        modeloTabela = new DefaultTableModel(COLUNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Task 04: so os componentes - edicao acontece em TelaEditarUsuario
            }
        };
        tabela = new JTable(modeloTabela);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // rodape: editar / remover (agem sobre a linha selecionada) + voltar
        JButton btnEditar = new JButton("Editar");
        JButton btnRemover = new JButton("Remover");
        JButton btnVoltar = new JButton("Voltar");
        btnEditar.addActionListener(this::btnEditarActionPerformed);
        btnRemover.addActionListener(this::btnRemoverActionPerformed);
        btnVoltar.addActionListener(this::btnVoltarActionPerformed);

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnEditar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnVoltar);

        add(painelTopo, BorderLayout.NORTH);
        add(scrollTabela, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
    }

    /**
     * Repovoa a tabela. {@code filtro} vazio/nulo traz todos
     * (Task 02 - listarTodos); um filtro preenchido usa a busca JPQL com
     * WHERE (Task 07 - buscarPorTermo), não mais um filtro em memória.
     */
    private void atualizarTabela(String filtro) {
        List<Usuario> resultado;
        try {
            resultado = (filtro == null || filtro.trim().isEmpty())
                    ? repositorio.listarTodos()
                    : repositorio.buscarPorTermo(filtro.trim());
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel carregar os usuarios: " + ex.getMessage(),
                    "Listagem", JOptionPane.ERROR_MESSAGE);
            return;
        }

        modeloTabela.setRowCount(0);
        usuariosExibidos.clear();
        for (Usuario usuario : resultado) {
            usuariosExibidos.add(usuario);
            modeloTabela.addRow(new Object[]{
                    usuario.getLogin(),
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getStatus()
            });
        }
    }

    private void btnBuscarActionPerformed(ActionEvent e) {
        atualizarTabela(txtBusca.getText());
    }

    private void btnNovoActionPerformed(ActionEvent e) {
        dispose();
        new TelaCadastroUsuario().setVisible(true);
    }

    private void btnEditarActionPerformed(ActionEvent e) {
        Usuario selecionado = usuarioSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuario na lista para editar.",
                    "Editar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Atividade 17 - Task 04/Task 05: so o LOGIN viaja para a tela de
        // edicao; e ela quem busca o usuario de novo no banco (e pode
        // lancar UsuarioNaoEncontradoException se ele tiver sumido nesse
        // meio-tempo).
        dispose();
        new TelaEditarUsuario(selecionado.getLogin()).setVisible(true);
    }

    private void btnRemoverActionPerformed(ActionEvent e) {
        Usuario selecionado = usuarioSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuario na lista para remover.",
                    "Remover", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacao = JOptionPane.showConfirmDialog(this,
                "Remover o usuario \"" + selecionado.getLogin() + "\"?",
                "Remover", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmacao != JOptionPane.YES_OPTION) {
            return;
        }

        // Atividade 17 - Task 02/Task 05/Task 06: remove no banco, dentro
        // de uma transacao (dentro de repositorio.remover); qualquer falha
        // e capturada e mostrada, sem derrubar a tela.
        try {
            repositorio.remover(selecionado);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel remover o usuario: " + ex.getMessage(),
                    "Remover", JOptionPane.ERROR_MESSAGE);
            return;
        }

        atualizarTabela(txtBusca.getText());
    }

    private void btnVoltarActionPerformed(ActionEvent e) {
        dispose();
        new TelaLogin().setVisible(true);
    }

    private Usuario usuarioSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || linha >= usuariosExibidos.size()) {
            return null;
        }
        return usuariosExibidos.get(linha);
    }
}
