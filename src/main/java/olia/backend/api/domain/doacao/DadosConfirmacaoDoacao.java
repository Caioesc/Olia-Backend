package olia.backend.api.domain.doacao;

public record DadosConfirmacaoDoacao(
    Long idDoacao,
    Double quantidadeReal // Caso a escola precise ajustar o valor
) {}
