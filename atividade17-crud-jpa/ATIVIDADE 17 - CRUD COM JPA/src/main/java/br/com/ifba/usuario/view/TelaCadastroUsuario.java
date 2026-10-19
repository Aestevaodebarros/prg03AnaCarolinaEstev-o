package br.com.ifba.usuario.view;

import br.com.ifba.login.view.TelaLogin;
import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repositorio.RepositorioUsuario;
import br.com.ifba.usuario.validar.ValidadorUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Tela de Cadastro de Usuario.
 * Aberta a partir do link "Nao tenho conta? Cadastre-se" da Tela de Login.
 *
 * Atividade 17 - Task 02: desde que o repositório passou a ser
 * {@link RepositorioUsuario} (JPA de verdade, Atividade 17), cada tela
 * cria sua própria instância do repositório em vez de receber uma
 * compartilhada por construtor (como era na Atividade 15, quando o
 * repositório guardava a List/Map em memória e precisava ser O MESMO
 * objeto em todas as telas). Agora o estado que importa é o banco, não o
 * objeto repositório — qualquer instância de RepositorioUsuario enxerga
 * os mesmos dados, porque todas usam a mesma fábrica (JPAUtil).
 */
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

    private final RepositorioUsuario repositorio = new RepositorioUsuario();

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

    // metodo chamado ao clicar no botao Cadastrar
    // [Atividade 10 - Task 02] este listener apenas captura os dados da tela,
    // chama as validacoes de ValidadorUsuario e exibe a mensagem
    // correspondente. Nenhuma regra de negocio (o que e um CPF valido, o
    // que e uma senha forte, etc.) fica aqui dentro.
    private void btnCadastrarActionPerformed(ActionEvent e) {

        // 1. captura o que foi digitado nos campos
        String nome = txtNome.getText();
        String cpf = txtCpf.getText();
        String genero = (String) cbxGenero.getSelectedItem();
        String dataNascimento = txtDataNascimento.getText();
        String telefone = txtTelefone.getText();
        String email = txtEmail.getText();
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());
        String confirmarSenha = new String(txtConfirmarSenha.getPassword());

        // 2. valida se algum campo esta vazio
        if (!ValidadorUsuario.camposPreenchidos(nome, cpf, dataNascimento, telefone,
                email, login, senha, confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.",
                    "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. valida o CPF
        if (!ValidadorUsuario.cpfValido(cpf)) {
            JOptionPane.showMessageDialog(this, "CPF invalido. Informe 11 digitos numericos.",
                    "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. valida a forca da senha
        if (!ValidadorUsuario.senhaForte(senha)) {
            JOptionPane.showMessageDialog(this,
                    "Senha fraca. Use pelo menos 6 caracteres, com letras e numeros.",
                    "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 5. valida se a senha confere com a confirmacao
        if (!ValidadorUsuario.senhasConferem(senha, confirmarSenha)) {
            JOptionPane.showMessageDialog(this, "As senhas nao coincidem.",
                    "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 6. instancia o objeto de dominio e preenche cada atributo
        Usuario usuario = new Usuario(nome, cpf, genero, dataNascimento, telefone, email, login, senha);

        // 7. valida se o login contem alguma palavra proibida (metodo estatico)
        if (ValidadorUsuario.contemPalavraProibida(usuario.getLogin())) {
            JOptionPane.showMessageDialog(this, "Login contem palavra nao permitida.",
                    "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 8. checa duplicidade consultando o banco de verdade
        if (repositorio.buscarPorLogin(usuario.getLogin()) != null) {
            JOptionPane.showMessageDialog(this, "Ja existe um usuario cadastrado com esse login.",
                    "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 9. [Atividade 17 - Task 02/Task 05/Task 06] grava no banco dentro
        // de uma transacao (dentro de repositorio.cadastrar); qualquer
        // falha (conexao caiu, coluna unique violada, etc.) sobe como
        // RuntimeException, e e capturada aqui para virar uma mensagem
        // legivel - a aplicacao nao quebra, mas tambem nao finge que deu
        // certo.
        try {
            repositorio.cadastrar(usuario);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Nao foi possivel cadastrar o usuario: " + ex.getMessage(),
                    "Cadastro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 10. tudo certo: exibe sucesso e volta para o login.
        JOptionPane.showMessageDialog(this, "Usuario " + usuario.getNome() + " cadastrado com sucesso!",
                "Cadastro", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new TelaLogin().setVisible(true);
    }

    // metodo chamado ao clicar no botao Cancelar: volta para a Tela de Login
    private void btnCancelarActionPerformed(ActionEvent e) {
        dispose();
        new TelaLogin().setVisible(true);
    }
}
