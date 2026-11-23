package br.com.ifba.usuario.controller;

import br.com.ifba.usuario.service.UsuarioService;
import org.springframework.stereotype.Controller;

@Controller
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }
}
