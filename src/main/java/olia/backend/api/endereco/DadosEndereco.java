package olia.backend.api.endereco;

import jakarta.validation.constraints.NotBlank;

public record DadosEndereco(
        @NotBlank
        String logradouro,

        @NotBlank
        String bairro,

        @NotBlank
        String cidade ) {
}
