package olia.backend.api.usuario;

import jakarta.validation.constraints.NotNull;
import olia.backend.api.endereco.DadosEndereco;

public record DadosAtualizacaoUsuario(

        @NotNull
        Long id,

        String nome,

        String email,

        String telefone,

        DadosEndereco endereco) {
}
