package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;

import java.util.Objects;

/**
 * Classe base do domínio "usuário" do Sistema Acadêmico (SUAP-IFBA).
 *
 * Atividade 11:
 * - Task 02: guarda um {@link Endereco} como atributo (composição).
 * - Task 04: guarda um {@link StatusUsuario} (enum) em vez de String livre.
 *
 * {@link Aluno} e {@link Professor} herdam desta classe (Task 01). Usuario
 * continua concreta (não abstrata) de propósito: TelaLogin ainda cria um
 * Usuario "genérico" só com login/senha (não há repositório de usuários
 * para buscar um Aluno/Professor já cadastrado), e TelaCadastroUsuario cria
 * um Usuario genérico completo — nenhuma das duas telas precisou mudar.
 */
public class Usuario implements Autenticavel {

    private final String nome;
    private final String cpf;
    private final String genero;
    private final String dataNascimento;
    private final String telefone;
    private final String email;
    private final String login;
    private final String senha;

    private Endereco endereco;
    private StatusUsuario status;

    /**
     * Construtor completo, usado no cadastro (Tela de Cadastro de Usuario).
     */
    public Usuario(String nome, String cpf, String genero, String dataNascimento,
                    String telefone, String email, String login, String senha) {
        this.nome = nome;
        this.cpf = cpf;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.email = email;
        this.login = Objects.requireNonNull(login, "login não pode ser nulo");
        this.senha = Objects.requireNonNull(senha, "senha não pode ser nula");
        this.status = StatusUsuario.ATIVO; // Task 05: nasce com o status correto
    }

    /**
     * Construtor simplificado, usado na Tela de Login, onde apenas login e
     * senha são digitados.
     */
    public Usuario(String login, String senha) {
        this(null, null, null, null, null, null, login, senha);
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getGenero() {
        return genero;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = Objects.requireNonNull(status, "status não pode ser nulo");
    }

    /**
     * Atividade 12 - Task 02 (método HERDADO): mensagem de boas-vindas
     * genérica, igual para qualquer tipo de usuário. Nem {@link Aluno} nem
     * {@link Professor} sobrescrevem este método — as duas subclasses usam
     * exatamente esta implementação, herdada sem alteração.
     */
    public String saudacao() {
        String nomeExibicao = (nome != null && !nome.isEmpty()) ? nome : login;
        return "Bem-vindo(a), " + nomeExibicao + "!";
    }

    /**
     * Atividade 12 - Task 02 (método SOBRESCRITO): descrição de perfil.
     * Aqui, na classe mãe, é só um valor "genérico" de fallback — cada
     * subclasse sobrescreve este método com {@code @Override} para devolver
     * sua própria descrição (polimorfismo).
     */
    public String descreverPerfil() {
        return "Usuario (login: " + login + ")";
    }

    /**
     * Implementação de Autenticavel. A comparação é feita inteiramente
     * dentro da classe, usando this.login e this.senha (que continuam
     * private): nenhum código externo precisa ler esses atributos para
     * saber se o acesso deve ser liberado.
     *
     * Melhoria desta versão: usuários com status diferente de ATIVO (ex.:
     * INATIVO ou PENDENTE) não conseguem autenticar, mesmo com a senha
     * correta. Como todo usuário nasce ATIVO por padrão, isso não muda o
     * comportamento de nenhum teste/tela existente — só passa a valer
     * quando alguém chamar setStatus(...) explicitamente.
     */
    @Override
    public boolean autenticar(String login, String senha) {
        if (login == null || senha == null) {
            return false;
        }
        return login.equals(this.login)
                && senha.equals(this.senha)
                && status == StatusUsuario.ATIVO;
    }

    @Override
    public String toString() {
        return String.format("Usuario{login='%s', nome='%s', status=%s}", login, nome, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Usuario)) {
            return false;
        }
        Usuario usuario = (Usuario) o;
        return Objects.equals(login, usuario.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login);
    }
}
