package olia.backend.api.domain.recompensa;

import java.time.format.DateTimeFormatter;

public record DadosListagemSolicitacao(
    Long id,
    String escola,
    String premio,
    Integer custo,
    String data,
    StatusSolicitacao status
) {
    public DadosListagemSolicitacao(SolicitacaoRecompensa s) {
        this(
            s.getId(),
            s.getEscola().getNome(),
            s.getRecompensa().getTitulo(),
            s.getRecompensa().getCusto(),
            s.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            s.getStatus()
        );
    }
}
