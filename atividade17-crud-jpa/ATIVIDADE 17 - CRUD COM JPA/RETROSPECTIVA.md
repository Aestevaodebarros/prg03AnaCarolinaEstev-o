# Retrospectiva

## Atividade 14 - Task 06 (a pergunta do DTO)

Olhando `Usuario` (a Entity), **`senha`** e **`cpf`** não deveriam aparecer
numa tela de listagem — senha é credencial sensível que não tem motivo
nenhum de sair do backend, e cpf é dado sensível que só importa em telas
de detalhe/edição, não numa lista de "quem está cadastrado".
`TelaListarUsuarios` já reflete essa resposta na prática: a tabela mostra
só login, nome, email e status.
