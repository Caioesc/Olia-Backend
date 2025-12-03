package olia.backend.api.domain.governo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroGoverno(
    @NotBlank String nome,
    @NotBlank @Email String email,
    @NotBlank String senha,
    @NotBlank String orgao,
    @NotBlank String matricula
) {}
