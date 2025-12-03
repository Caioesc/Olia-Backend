package olia.backend.api.domain.coleta;

import java.time.format.DateTimeFormatter;

public record DadosListagemColeta(
    Long id,
    String escola,
    String endereco,
    String quantidade,
    String dataPreferida,
    StatusColeta status
) {
    public DadosListagemColeta(Coleta coleta) {
        this(
            coleta.getId(),
            coleta.getEscola().getNome(),
            coleta.getEscola().getEndereco().getLogradouro(), // Pega a rua da escola
            coleta.getQuantidadeEstimada() + "L",
            coleta.getDataPreferida().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            coleta.getStatus()
        );
    }
}