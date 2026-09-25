package br.com.ifba.usuario.entity;

/**
 * Situação cadastral de um {@link Usuario} no Sistema Acadêmico.
 *
 * Atividade 11 - Task 04: enum com valores fixos, usado como tipo de um
 * atributo do domínio (em vez de representar o status como String livre,
 * o que permitiria valores inválidos como "atvio" ou "Ativo ").
 */
public enum StatusUsuario {
    ATIVO,
    INATIVO,
    PENDENTE
}
