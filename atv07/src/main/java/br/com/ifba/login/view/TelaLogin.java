package br.com.ifba.login.view;

import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.view.TelaCadastroUsuario;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

/**
 * Tela de Login do Sistema Academico.
 */
public class TelaLogin extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JLabel lblResultado;

    public TelaLogin() {
        montarTela();
    }

    private void montarTela() {
        setTitle("Tela de Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // topo: "logo" simples (label SA) e nome do sistema
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));
        JLabel lblLogo = new JLabel("SA", SwingConstants.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setOpaque(true);
        lblLogo.setBackground(new Color(52, 101, 164));
        lblLogo.setPreferredSize(new Dimension(70, 70));
        lblLogo.setMaximumSize(new Dimension(70, 70));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblNomeSistema = new JLabel("Sistema Academico", SwingConstants.CENTER);
        lblNomeSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNomeSistema.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel painelLogoWrapper = new JPanel();
        painelLogoWrapper.add(lblLogo);

        painelTopo.add(Box.createVerticalStrut(10));
        painelTopo.add(painelLogoWrapper);
        painelTopo.add(lblNomeSistema);
        painelTopo.add(Box.createVerticalStrut(10));

        // centro: campos de login e senha
        JPanel painelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        txtLogin = new JTextField(15);
        txtSenha = new JPasswordField(15);

        c.gridx = 0;
        c.gridy = 0;
        painelCampos.add(new JLabel("Login:"), c);
        c.gridx = 1;
        c.weightx = 1;
        painelCampos.add(txtLogin, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        painelCampos.add(new JLabel("Senha:"), c);
        c.gridx = 1;
        c.weightx = 1;
        painelCampos.add(txtSenha, c);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(this::btnEntrarActionPerformed);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 0;
        painelCampos.add(btnEntrar, c);

        // link "Nao tenho conta? Cadastre-se"
        JLabel lblSemConta = new JLabel("Nao tenho conta?", SwingConstants.CENTER);
        JLabel lblCadastreSe = new JLabel("<html><u>Cadastre-se</u></html>", SwingConstants.CENTER);
        lblCadastreSe.setForeground(new Color(52, 101, 164));
        lblCadastreSe.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblCadastreSe.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                lblCadastreSeMouseClicked(e);
            }
        });

        c.gridy = 3;
        painelCampos.add(lblSemConta, c);
        c.gridy = 4;
        painelCampos.add(lblCadastreSe, c);

        // rodape: label de resultado (preenchido ao clicar em Entrar)
        lblResultado = new JLabel("<html></html>");
        lblResultado.setOpaque(true);
        lblResultado.setBackground(new Color(255, 255, 224));
        lblResultado.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        lblResultado.setVerticalAlignment(SwingConstants.TOP);

        add(painelTopo, BorderLayout.NORTH);
        add(painelCampos, BorderLayout.CENTER);
        add(lblResultado, BorderLayout.SOUTH);
    }

    // metodo chamado ao clicar no botao Entrar
    private void btnEntrarActionPerformed(ActionEvent e) {

        // 1. captura o que foi digitado nos campos
        String loginDigitado = txtLogin.getText();
        String senhaDigitada = new String(txtSenha.getPassword());

        // 2. instancia o objeto de dominio e preenche login/senha
        Usuario usuario = new Usuario();
        usuario.login = loginDigitado;
        usuario.senha = senhaDigitada;

        // 3. usa os dados vindos do objeto (e nao mais direto dos campos) para montar o label
        lblResultado.setText(
                "<html>Login digitado: " + usuario.login + "<br>"
                        + "Senha digitada: " + usuario.senha + "</html>"
        );
    }

    // metodo chamado ao clicar no link "Cadastre-se": abre a TelaCadastroUsuario
    private void lblCadastreSeMouseClicked(MouseEvent e) {
        dispose();
        new TelaCadastroUsuario().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
