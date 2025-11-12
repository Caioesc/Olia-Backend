package olia.backend.api.escola;

import jakarta.validation.constraints.NotNull;
import olia.backend.api.endereco.DadosEndereco;

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
