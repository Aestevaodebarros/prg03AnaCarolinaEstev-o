package br.com.ifba.usuario.view;

import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.exception.UsuarioNaoEncontradoException;
import br.com.ifba.usuario.repositorio.RepositorioUsuario;
import br.com.ifba.usuario.validar.ValidadorUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Atividade 14 - Task 04 ("Telas do CRUD"): tela de EDIÇÃO de um Usuario
 * já cadastrado.
 *
 * Atividade 17 - Task 02/Task 04/Task 05: recebe apenas o LOGIN do
 * usuário (não o objeto já carregado pela listagem) e busca uma cópia
 * fresca no banco com
 * {@link RepositorioUsuario#buscarPorLoginOuFalhar(String)} — que lança
 * {@link UsuarioNaoEncontradoException} se ele não existir mais (Task 04:
 * ver o Javadoc daquele método para o "antes/depois"). Essa exceção é
 * capturada aqui, no construtor: se o usuário sumiu, mostramos a mensagem
 * num JOptionPane e fechamos esta tela em vez de deixá-la abrir "vazia"
 * e quebrar no primeiro campo preenchido.
 *
 * Login não é editável (fica num JTextField desabilitado): é a chave que
 * {@link Usuario#equals(Object)}/{@code hashCode()} usam.
 *
 * Senha é opcional aqui: deixar em branco mantém a senha atual.
 */
public class TelaEditarUsuario extends JFrame {

    private final RepositorioUsuario repositorio = new RepositorioUsuario();
    private final Usuario usuario;

    private JTextField txtLogin;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JComboBox<String> cbxGenero;
    private JTextField txtDataNascimento;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;

    /**
     * @throws UsuarioNaoEncontradoException se o usuário com este login já
     *                                        não existir mais no banco —
     *                                        quem chama este construtor
     *                                        (TelaListarUsuarios) não
     *                                        precisa tratar: a própria
     *                                        tela trata no bloco abaixo e
     *                                        se fecha sozinha.
     */
    public TelaEditarUsuario(String login) {
        Usuario usuarioCarregado;
        try {
            usuarioCarregado = repositorio.buscarPorLoginOuFalhar(login);
        } catch (UsuarioNaoEncontradoException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(),
                    "Editar", JOptionPane.ERROR_MESSAGE);
            this.usuario = null;
            dispose();
            return;
        }
        this.usuario = usuarioCarregado;
        montarTela();
        preencherCampos();
    }

    private void montarTela() {
        setTitle("Editar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 520);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtLogin = new JTextField(18);
        txtLogin.setEnabled(false);
        txtNome = new JTextField(18);
        txtCpf = new JTextField(18);
        cbxGenero = new JComboBox<>(new String[]{"Masculino", "Feminino", "Outro"});
        txtDataNascimento = new JTextField(18);
        txtTelefone = new JTextField(18);
        txtEmail = new JTextField(18);
        txtSenha = new JPasswordField(18);
        txtConfirmarSenha = new JPasswordField(18);

        int linha = 0;
        adicionarCampo(painel, c, linha++, "Login:", txtLogin);
        adicionarCampo(painel, c, linha++, "Nome completo:", txtNome);
        adicionarCampo(painel, c, linha++, "CPF:", txtCpf);
        adicionarCampo(painel, c, linha++, "Genero:", cbxGenero);
        adicionarCampo(painel, c, linha++, "Data de Nascimento:", txtDataNascimento);
        adicionarCampo(painel, c, linha++, "Telefone:", txtTelefone);
        adicionarCampo(painel, c, linha++, "Email:", txtEmail);
        adicionarCampo(painel, c, linha++, "Nova Senha (opcional):", txtSenha);
        adicionarCampo(painel, c, linha++, "Confirmar Nova Senha:", txtConfirmarSenha);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");
        btnSalvar.addActionListener(this::btnSalvarActionPerformed);
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnCancelar);

        c.gridx = 0;
        c.gridy = linha;
        c.gridwidth = 2;
        painel.add(painelBotoes, c);

        add(painel);
    }

    private void adicionarCampo(JPanel painel, GridBagConstraints c, int linha, String rotulo, JComponent campo) {
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = linha;
        c.weightx = 0;
        painel.add(new JLabel(rotulo), c);

        c.gridx = 1;
        c.weightx = 1;
        painel.add(campo, c);
    }

    private void preencherCampos() {
        txtLogin.setText(usuario.getLogin());
        txtNome.setText(usuario.getNome());
        txtCpf.setText(usuario.getCpf());
        cbxGenero.setSelectedItem(usuario.getGenero());
        txtDataNascimento.setText(usuario.getDataNascimento());
        txtTelefone.setText(usuario.getTelefone());
        txtEmail.setText(usuario.getEmail());
    }

    private void btnSalvarActionPerformed(ActionEvent e) {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String genero = (String) cbxGenero.getSelectedItem();
        String dataNascimento = txtDataNascimento.getText();
        String telefone = txtTelefone.getText();
        String email = txtEmail.getText();
        String novaSenha = new String(txtSenha.getPassword());
        String confirmarNovaSenha = new String(txtConfirmarSenha.getPassword());

        if (!ValidadorUsuario.camposPreenchidos(nome, cpf, dataNascimento, telefone, email)) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.",
                    "Editar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!ValidadorUsuario.cpfValido(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF invalido. Informe 11 digitos numericos.",
                    "Editar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // senha so e trocada se o campo "Nova Senha" foi preenchido
        if (!novaSenha.isEmpty() || !confirmarNovaSenha.isEmpty()) {
            if (!ValidadorUsuario.senhaForte(novaSenha)) {
                JOptionPane.showMessageDialog(this,
                        "Senha fraca. Use pelo menos 6 caracteres, com letras e numeros.",
                        "Editar", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!ValidadorUsuario.senhasConferem(novaSenha, confirmarNovaSenha)) {
                JOptionPane.showMessageDialog(this, "As senhas nao coincidem.",
                        "Editar", JOptionPane.ERROR_MESSAGE);
                return;
            }
            usuario.setSenha(novaSenha);
        }

        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setGenero(genero);
        usuario.setDataNascimento(dataNascimento);
        usuario.setTelefone(telefone);
        usuario.setEmail(email);

        // Atividade 17 - Task 02/Task 05/Task 06: grava a edicao no banco
        // com merge (dentro de repositorio.editar), dentro de uma
        // transacao. Falhas sobem como RuntimeException e sao mostradas
        // aqui, sem derrubar a tela.
        try {
            repositorio.editar(usuario);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel salvar as alteracoes: " + ex.getMessage(),
                    "Editar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Usuario " + usuario.getNome() + " atualizado com sucesso!",
                "Editar", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new TelaListarUsuarios().setVisible(true);
    }

    private void btnCancelarActionPerformed(ActionEvent e) {
        dispose();
        new TelaListarUsuarios().setVisible(true);
    }
}
