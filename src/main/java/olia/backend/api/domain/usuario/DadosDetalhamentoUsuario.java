package olia.backend.api.domain.usuario;

import olia.backend.api.domain.endereco.Endereco;

public record DadosDetalhamentoUsuario(Long id, String nome, String email, String cpf, String telefone, String numeroNis, boolean temBolsaFamilia, Endereco endereco) {

    public DadosDetalhamentoUsuario(Usuario usuario){
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCpf(), usuario.getTelefone(), usuario.getNumeroNis(), usuario.isTemBolsaFamilia(), usuario.getEndereco());
    }
}
