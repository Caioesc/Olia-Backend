package olia.backend.api.domain.escola;

import jakarta.validation.constraints.NotNull;
import olia.backend.api.domain.endereco.DadosEndereco;

public record DadosAtualizacaoEscola(

        @NotNull
        Long id,

        String nome,

        String telefone,

        String email,

        String email_acesso,

        String senha,

        String nome_responsavel,

        Capacidade capacidade,

        DadosEndereco endereco) {
}
