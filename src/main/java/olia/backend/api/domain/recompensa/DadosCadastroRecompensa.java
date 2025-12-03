package olia.backend.api.domain.recompensa;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroRecompensa(
    Long id,
    String titulo,
    @NotNull Integer custo,
    String icone
) {}