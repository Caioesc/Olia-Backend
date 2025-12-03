package olia.backend.api.domain.recompensa;
import jakarta.validation.constraints.NotNull;

public record DadosSolicitarRecompensa(
    @NotNull Long idRecompensa,
    @NotNull Long idEscola
) {}
