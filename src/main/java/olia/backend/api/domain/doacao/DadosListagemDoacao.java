package olia.backend.api.domain.doacao;

import java.time.format.DateTimeFormatter;

public record DadosListagemDoacao(
    Long id,
    String escola,
    String usuarioNome,
    String data,
    String quantidade,
    String codigo,
    StatusDoacao status
) {
    // Construtor para converter a Entidade em DTO
    public DadosListagemDoacao(Doacao doacao) {
        this(
            doacao.getId(),
            doacao.getEscola().getNome(),
            doacao.getUsuario().getNome(),
            doacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            doacao.getQuantidade() + "L",
            doacao.getCodigo(),
            doacao.getStatus()
        );
    }
}
