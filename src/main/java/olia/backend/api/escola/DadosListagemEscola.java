package olia.backend.api.escola;

import olia.backend.api.endereco.DadosEndereco;
import olia.backend.api.endereco.Endereco;

public record DadosListagemEscola(Long id, String nome, String nome_responsavel, String email, String telefone, String cnpj, String codigo_inep, Endereco endereco) {

    public DadosListagemEscola(Escola escola){
        this(escola.getId(), escola.getNome(), escola.getNome_responsavel(), escola.getEmail(), escola.getTelefone(), escola.getCnpj(), escola.getCodigo_inep(), escola.getEndereco());
    }
}
