package br.com.ifba.usuario.view;

import br.com.ifba.usuario.entity.Usuario;
import br.com.ifba.usuario.repositorio.RepositorioUsuarioEmMemoria;
import br.com.ifba.usuario.validar.ValidadorUsuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

/**
 * Atividade 14 - Task 04 ("Telas do CRUD"): tela de EDIÇÃO de um Usuario
 * já cadastrado.
 *
 * Login não é editável (fica num JTextField desabilitado): é a chave que
 * {@link Usuario#equals(Object)}/{@code hashCode()} usam e o índice que
 * {@link RepositorioUsuarioEmMemoria} mantém em memória (Atividade 15 -
 * Task 03); mudar o login de um objeto já guardado desalinharia esse
 * índice. Trocar de login, se um dia for preciso, é "remover + cadastrar
 * de novo" — não uma edição.
 *
 * Senha é opcional aqui: deixar em branco mantém a senha atual.
 */
public class TelaEditarUsuario extends JFrame {

    private final RepositorioUsuarioEmMemoria repositorio;
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

    public TelaEditarUsuario(RepositorioUsuarioEmMemoria repositorio, Usuario usuario) {
        this.repositorio = repositorio;
        this.usuario = Objects.requireNonNull(usuario, "usuario não pode ser nulo");
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

        // objeto editado em memoria e a MESMA instancia guardada no
        // repositorio (List/Map) - nao ha "salvar de volta" a fazer aqui.
        usuario.setNome(nome);
        usuario.setCpf(cpf);
        usuario.setGenero(genero);
        usuario.setDataNascimento(dataNascimento);
        usuario.setTelefone(telefone);
        usuario.setEmail(email);

        JOptionPane.showMessageDialog(this, "Usuario " + usuario.getNome() + " atualizado com sucesso!",
                "Editar", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new TelaListarUsuarios(repositorio).setVisible(true);
    }

    private void btnCancelarActionPerformed(ActionEvent e) {
        dispose();
        new TelaListarUsuarios(repositorio).setVisible(true);
    }
}
