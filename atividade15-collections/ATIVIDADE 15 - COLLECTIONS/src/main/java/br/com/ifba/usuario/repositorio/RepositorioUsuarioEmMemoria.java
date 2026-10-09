package br.com.ifba.usuario.repositorio;

import br.com.ifba.usuario.entity.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Atividade 15 - "Guardando os seus objetos".
 *
 * Até aqui, os {@link Usuario} nasciam, respondiam a uma chamada e morriam
 * quando a tela fechava. Esta classe é o lugar onde eles passam a ser
 * guardados de verdade, para que TelaCadastroUsuario e TelaLogin
 * compartilhem os mesmos usuários (Task 01).
 */
public class RepositorioUsuarioEmMemoria {

    // Task 01: List<Usuario>, e não List "crua" — o generics diz ao
    // compilador o que existe dentro da coleção, então cada objeto que sai
    // daqui já vem como Usuario, sem precisar de cast.
    private final List<Usuario> usuarios = new ArrayList<>();

    // Task 03: índice por login, alimentado a cada cadastrar(...).
    private final Map<String, Usuario> porLogin = new HashMap<>();

    /**
     * Task 01: cadastra um usuário no repositório.
     *
     * Task 02: isto só funciona de forma confiável porque {@link Usuario}
     * sabe se comparar (equals/hashCode por login) — é o que faz
     * {@code porLogin.put(...)} e qualquer futura checagem de duplicidade
     * (ex.: {@code usuarios.contains(...)}) enxergarem dois objetos com o
     * mesmo login como "o mesmo usuário", mesmo sendo instâncias
     * diferentes.
     */
    public void cadastrar(Usuario usuario) {
        Objects.requireNonNull(usuario, "usuario não pode ser nulo");
        usuarios.add(usuario);
        porLogin.put(usuario.getLogin(), usuario);
    }

    /**
     * Task 01: devolve todos os usuários cadastrados. List.copyOf devolve
     * uma cópia imutável — quem chama este método não consegue alterar a
     * lista interna do repositório por fora da classe.
     */
    public List<Usuario> listarTodos() {
        return List.copyOf(usuarios);
    }

    /**
     * Task 03 (primeira versão): busca linear, percorrendo a List com um
     * for até achar o login ou chegar ao fim.
     *
     * Custo O(n). Com dez usuários isso é instantâneo, nem dá para medir a
     * diferença. Com dez mil usuários, no pior caso (login não existe, ou
     * é o último da lista) o laço compara os dez mil, um por um, toda vez
     * que alguém tenta entrar — e esse custo cresce de forma linear com o
     * tamanho da base. Mantida aqui só para comparação; não é a versão
     * usada por {@link #buscarPorLogin(String)}.
     */
    public Usuario buscarPorLoginComFor(String login) {
        for (Usuario usuario : usuarios) {
            if (usuario.getLogin().equals(login)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Task 03 (segunda versão, a usada pelo repositório): busca indexada,
     * usando o HashMap {@link #porLogin}, alimentado a cada
     * {@link #cadastrar(Usuario)}.
     *
     * Custo O(1) em média, porque o HashMap calcula o hashCode do login e
     * vai direto ao balde certo — não importa se há dez ou dez mil
     * usuários, o tempo de busca não cresce com o tamanho da base.
     */
    public Usuario buscarPorLogin(String login) {
        return porLogin.get(login);
    }
}
