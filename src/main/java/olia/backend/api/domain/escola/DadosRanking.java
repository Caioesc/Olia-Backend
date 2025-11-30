package olia.backend.api.domain.escola;

public record DadosRanking(
    int posicao,
    String nome,
    Integer pontos,
    Double litros,
    boolean ehMinhaEscola
) {}
