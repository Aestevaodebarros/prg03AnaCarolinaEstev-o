package br.com.ifba.usuario.view;

import br.com.ifba.login.view.TelaLogin;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.validar.ValidadorUsuario;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class TelaCadastroUsuario extends JFrame {

    private JTextField txtNome;
    private JTextField txtCpf;
    private JComboBox<String> cbxGenero;
    private JTextField txtDataNascimento;
    private JTextField txtTelefone;
    private JTextField txtEmail;
    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;

    public TelaCadastroUsuario() {
        montarTela();
    }

    private void montarTela() {
        setTitle("Tela de Cadastro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 480);
        setLocationRelativeTo(null);

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(18);
        txtCpf = new JTextField(18);
        cbxGenero = new JComboBox<>(new String[]{"Masculino", "Feminino", "Outro"});
        txtDataNascimento = new JTextField(18);
        txtTelefone = new JTextField(18);
        txtEmail = new JTextField(18);
        txtLogin = new JTextField(18);
        txtSenha = new JPasswordField(18);
        txtConfirmarSenha = new JPasswordField(18);

        int linha = 0;
        adicionarCampo(painel, c, linha++, "Nome completo:", txtNome);
        adicionarCampo(painel, c, linha++, "CPF:", txtCpf);
        adicionarCampo(painel, c, linha++, "Genero:", cbxGenero);
        adicionarCampo(painel, c, linha++, "Data de Nascimento:", txtDataNascimento);
        adicionarCampo(painel, c, linha++, "Telefone:", txtTelefone);
        adicionarCampo(painel, c, linha++, "Email:", txtEmail);
        adicionarCampo(painel, c, linha++, "Login:", txtLogin);
        adicionarCampo(painel, c, linha++, "Senha:", txtSenha);
        adicionarCampo(painel, c, linha++, "Confirmar Senha:", txtConfirmarSenha);

        JButton btnCadastrar = new JButton("Cadastrar");
        JButton btnCancelar = new JButton("Cancelar");
        btnCadastrar.addActionListener(this::btnCadastrarActionPerformed);
        btnCancelar.addActionListener(this::btnCancelarActionPerformed);

        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnCadastrar);
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

    private void btnCadastrarActionPerformed(ActionEvent e) {
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String genero = (String) cbxGenero.getSelectedItem();
        String dataNascimento = txtDataNascimento.getText();
        String telefone = txtTelefone.getText();
        String email = txtEmail.getText();
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        if (nome.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty()
                || telefone.isEmpty() || email.isEmpty() || login.isEmpty()
                || senha.isEmpty() || confirmarSenha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.", "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas nao coincidem.", "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Task 03 - Instancia o Usuario usando o construtor com parametros.
        // Nenhum atributo e acessado diretamente: apenas o construtor e os getters.
        Usuario usuario = new Usuario(nome, cpf, genero, dataNascimento, telefone, email, login, senha);

        if (ValidadorUsuario.contemPalavraProibida(usuario.getLogin())) {
            JOptionPane.showMessageDialog(this, "Login contem palavra nao permitida.", "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Usuario " + usuario.getNome() + " cadastrado com sucesso!",
                "Cadastro",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void btnCancelarActionPerformed(ActionEvent e) {
        dispose();
        new TelaLogin().setVisible(true);
    }
}
