package br.com.ifba.login.view;

import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.view.TelaCadastroUsuario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

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

        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        JLabel lblLogo = new JLabel("SA", JLabel.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 28));
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setOpaque(true);
        lblLogo.setBackground(new Color(52, 101, 164));
        lblLogo.setPreferredSize(new Dimension(70, 70));
        lblLogo.setMaximumSize(new Dimension(70, 70));
        lblLogo.setHorizontalAlignment(JLabel.CENTER);

        JLabel lblNomeSistema = new JLabel("Sistema Academico", JLabel.CENTER);
        lblNomeSistema.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNomeSistema.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel painelLogoWrapper = new JPanel();
        painelLogoWrapper.add(lblLogo);

        painelTopo.add(Box.createVerticalStrut(10));
        painelTopo.add(painelLogoWrapper);
        painelTopo.add(lblNomeSistema);
        painelTopo.add(Box.createVerticalStrut(10));

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
        c.weightx = 1.0;
        painelCampos.add(txtLogin, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        painelCampos.add(new JLabel("Senha:"), c);
        c.gridx = 1;
        c.weightx = 1.0;
        painelCampos.add(txtSenha, c);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(this::btnEntrarActionPerformed);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 0;
        painelCampos.add(btnEntrar, c);

        JLabel lblSemConta = new JLabel("Nao tenho conta?", JLabel.CENTER);
        JLabel lblCadastreSe = new JLabel("<html><u>Cadastre-se</u></html>", JLabel.CENTER);
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

        lblResultado = new JLabel("<html></html>");
        lblResultado.setOpaque(true);
        lblResultado.setBackground(new Color(255, 255, 224));
        lblResultado.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        lblResultado.setVerticalAlignment(JLabel.TOP);

        add(painelTopo, BorderLayout.NORTH);
        add(painelCampos, BorderLayout.CENTER);
        add(lblResultado, BorderLayout.SOUTH);
    }

    private void btnEntrarActionPerformed(ActionEvent e) {
        String loginDigitado = txtLogin.getText();
        String senhaDigitada = new String(txtSenha.getPassword());

        // Encapsulamento: Usuario e criado pelo construtor e lido pelos getters,
        // nenhum atributo e acessado diretamente de fora da classe.
        Usuario usuario = new Usuario(loginDigitado, senhaDigitada);

        lblResultado.setText("<html>Login digitado: " + usuario.getLogin()
                + "<br>Senha digitada: " + usuario.getSenha() + "</html>");
    }

    private void lblCadastreSeMouseClicked(MouseEvent e) {
        dispose();
        new TelaCadastroUsuario().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}
