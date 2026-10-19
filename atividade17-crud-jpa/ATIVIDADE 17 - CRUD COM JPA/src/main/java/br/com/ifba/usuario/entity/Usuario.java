package br.com.ifba.usuario.entity;

import br.com.ifba.usuario.interfaces.Autenticavel;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
 *
 * Atividade 14 - Task 02: classe central do domínio, mapeada como entidade
 * JPA. "usuario" sozinho colidiria com a palavra reservada USER em alguns
 * bancos (ex.: MySQL/Postgres), por isso @Table renomeia para "tb_usuario";
 * o mesmo vale para "senha" -> "nm_senha" só por padronização de prefixo
 * das colunas sensíveis do projeto. As demais colunas usam o nome padrão
 * (igual ao atributo), exceto dataNascimento, que no banco segue o padrão
 * snake_case (data_nascimento) em vez do camelCase do Java.
 */
@Entity
@Table(name = "tb_usuario")
public class Usuario implements Autenticavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true, length = 11)
    private String cpf;

    private String genero;

    @Column(name = "data_nascimento")
    private String dataNascimento;

    private String telefone;

    @Column(unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(name = "nm_senha", nullable = false)
    private String senha;

    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusUsuario status;

    /**
     * Construtor vazio exigido pelo JPA/Hibernate (Atividade 14 - Task 05):
     * o provider instancia a entidade por reflexão antes de popular os
     * campos a partir do ResultSet, então precisa de um construtor sem
     * argumentos acessível ao menos no próprio pacote. Não é usado em
     * nenhum outro lugar do código "de mão" — por isso é protected, não
     * public: continua não dando pra criar um Usuario "vazio" por acidente
     * fora do pacote entity.
     */
    protected Usuario() {
    }

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

    public Long getId() {
        return id;
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

    /*
     * Atividade 14 - Task 04: setters abaixo existem só para a Tela de
     * Edição (TelaEditarUsuario) poder alterar um Usuario já cadastrado.
     * login não tem setter de propósito: é a chave usada por equals/
     * hashCode e pelo índice do repositório (Map<String, Usuario>) —
     * trocar o login de um objeto já guardado quebraria essa indexação.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = Objects.requireNonNull(senha, "senha não pode ser nula");
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
     * Atividade 13 - Task 02: o método devolve a mensagem em vez de
     * imprimi-la. Usuários com status diferente de ATIVO (ex.: INATIVO ou
     * PENDENTE) não conseguem autenticar, mesmo com a senha correta. Como
     * todo usuário nasce ATIVO por padrão, isso não muda o comportamento
     * de nenhuma tela existente — só passa a valer quando alguém chamar
     * setStatus(...) explicitamente.
     */
    @Override
    public String autenticar(String login, String senha) {
        boolean credenciaisConferem = login != null && senha != null
                && login.equals(this.login) && senha.equals(this.senha);
        if (credenciaisConferem && status == StatusUsuario.ATIVO) {
            return "Bem-vindo, " + login;
        }
        return "Acesso negado";
    }

    @Override
    public String toString() {
        return String.format("Usuario{login='%s', nome='%s', status=%s}", login, nome, status);
    }

    /**
     * Atividade 15 - Task 02: dois Usuario são "iguais" se tiverem o mesmo
     * login. Login foi escolhido em vez de CPF porque é o campo que o
     * próprio sistema usa para autenticar e indexar (ver
     * RepositorioUsuarioEmMemoria.porLogin) — é a identidade que já importa
     * para o domínio, e nem todo Usuario tem CPF preenchido (o construtor
     * simplificado da Tela de Login só tem login/senha).
     */
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
