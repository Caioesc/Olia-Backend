package olia.backend.api.domain.usuario;

import jakarta.validation.constraints.NotNull;
import olia.backend.api.domain.endereco.DadosEndereco;

public record DadosAtualizacaoUsuario(

        @NotNull
        Long id,

        String nome,

        String email,

        String telefone,

        DadosEndereco endereco) {
}
