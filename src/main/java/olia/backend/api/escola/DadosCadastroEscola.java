package olia.backend.api.escola;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import olia.backend.api.endereco.DadosEndereco;
import olia.backend.api.endereco.Endereco;

public record DadosCadastroEscola(

        @NotBlank
        String nome,

        @NotBlank
        @Pattern(regexp = "\\d{14}")
        String cnpj,

        @NotBlank
        @Pattern(regexp = "\\d{8}")
        String codigo_inep,

        @NotNull
        @Valid
        DadosEndereco endereco,

        @NotNull
        Capacidade capacidade,

        @NotBlank
        String nome_responsavel,

        @NotBlank
        String telefone,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Email
        String email_acesso,

        @NotBlank
        String senha
) {
}
