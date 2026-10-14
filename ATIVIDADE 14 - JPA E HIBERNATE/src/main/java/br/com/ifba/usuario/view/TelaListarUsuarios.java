package br.com.ifba.usuario.view;

import br.com.ifba.login.view.TelaLogin;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repositorio.RepositorioUsuarioEmMemoria;

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
 * Esta tela usa o {@link RepositorioUsuarioEmMemoria} já construído na
 * Atividade 15 para listar/remover usuários — ainda não há nenhum código
 * de JPA/Hibernate aqui (nenhum EntityManager, nenhum em.persist/remove).
 * A Parte 2 troca o que está DENTRO do repositório por chamadas reais ao
 * banco; a assinatura pública usada por esta tela (listarTodos,
 * buscarPorLogin, remover) não muda.
 *
 * Atividade 14 - Task 02/Task 06: a tabela mostra só login, nome, email e
 * status — nunca cpf, dataNascimento ou senha. Essa escolha é também a
 * resposta da Task 06 (ver RETROSPECTIVA.md): dados sensíveis ou
 * irrelevantes para "identificar o usuário na lista" não deveriam viajar
 * até uma tela de listagem.
 */
public class TelaListarUsuarios extends JFrame {

    private static final String[] COLUNAS = {"Login", "Nome", "Email", "Status"};

    private final RepositorioUsuarioEmMemoria repositorio;
    private final List<Usuario> usuariosExibidos = new ArrayList<>();

    private JTextField txtBusca;
    private JTable tabela;
    private DefaultTableModel modeloTabela;

    public TelaListarUsuarios(RepositorioUsuarioEmMemoria repositorio) {
        this.repositorio = repositorio;
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
     * Repovoa a tabela a partir do repositório. Quando {@code filtro} não é
     * vazio, mantém só os usuários cujo login OU nome contêm o texto
     * digitado (busca simples em memória, sem nenhum SELECT envolvido).
     */
    private void atualizarTabela(String filtro) {
        modeloTabela.setRowCount(0);
        usuariosExibidos.clear();

        String filtroNormalizado = (filtro == null) ? "" : filtro.trim().toLowerCase();

        for (Usuario usuario : repositorio.listarTodos()) {
            boolean combina = filtroNormalizado.isEmpty()
                    || contem(usuario.getLogin(), filtroNormalizado)
                    || contem(usuario.getNome(), filtroNormalizado);

            if (combina) {
                usuariosExibidos.add(usuario);
                modeloTabela.addRow(new Object[]{
                        usuario.getLogin(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getStatus()
                });
            }
        }
    }

    private boolean contem(String texto, String filtroNormalizado) {
        return texto != null && texto.toLowerCase().contains(filtroNormalizado);
    }

    private void btnBuscarActionPerformed(ActionEvent e) {
        atualizarTabela(txtBusca.getText());
    }

    private void btnNovoActionPerformed(ActionEvent e) {
        dispose();
        new TelaCadastroUsuario(repositorio).setVisible(true);
    }

    private void btnEditarActionPerformed(ActionEvent e) {
        Usuario selecionado = usuarioSelecionado();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuario na lista para editar.",
                    "Editar", JOptionPane.WARNING_MESSAGE);
            return;
        }
        dispose();
        new TelaEditarUsuario(repositorio, selecionado).setVisible(true);
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

        if (confirmacao == JOptionPane.YES_OPTION) {
            repositorio.remover(selecionado);
            atualizarTabela(txtBusca.getText());
        }
    }

    private void btnVoltarActionPerformed(ActionEvent e) {
        dispose();
        new TelaLogin(repositorio).setVisible(true);
    }

    private Usuario usuarioSelecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0 || linha >= usuariosExibidos.size()) {
            return null;
        }
        return usuariosExibidos.get(linha);
    }
}
