package olia.backend.api.escola;

import olia.backend.api.endereco.Endereco;

public record DadosDetalhamentoEscola(Long id, String nome, String nome_responsavel, String email, String telefone, String email_acesso, String cnpj, String codigo_inep, Endereco endereco, Capacidade capacidade) {

    public DadosDetalhamentoEscola(Escola escola){
        this(escola.getId(), escola.getNome(), escola.getNome_responsavel(), escola.getEmail(), escola.getTelefone(), escola.getEmail_acesso(), escola.getCnpj(), escola.getCodigo_inep(), escola.getEndereco(), escola.getCapacidade());
    }
}
