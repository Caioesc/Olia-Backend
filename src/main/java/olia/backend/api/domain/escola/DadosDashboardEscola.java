package olia.backend.api.domain.escola;

public record DadosDashboardEscola(
    String nome,
    Double totalColetado,
    Integer coletasRealizadas,
    Integer pontos,
    Double capacidadeMaxima,
    Double ocupacaoAtual,
    Double porcentagemOcupacao,
    Integer metaPontos
) {}
