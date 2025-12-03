package olia.backend.api.domain.coleta;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record DadosSolicitacaoColeta(
    @NotNull Long idEscola,
    @NotNull Double quantidade,
    @NotNull LocalDate data
) {}
