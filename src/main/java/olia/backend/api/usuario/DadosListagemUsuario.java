package olia.backend.api.usuario;

//DTO criado para devolver apenas os dados necessários para listagem
public record DadosListagemUsuario(Long id, String nome, String email) {

    public DadosListagemUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail());
    }
}
