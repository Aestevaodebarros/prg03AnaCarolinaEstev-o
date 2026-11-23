package br.com.ifba.usuario.service;

import br.com.ifba.usuario.repositorio.RepositorioUsuario;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final RepositorioUsuario repositorioUsuario;

    public UsuarioService() {
        this.repositorioUsuario = new RepositorioUsuario();
    }

    public RepositorioUsuario getRepositorioUsuario() {
        return repositorioUsuario;
    }
}
