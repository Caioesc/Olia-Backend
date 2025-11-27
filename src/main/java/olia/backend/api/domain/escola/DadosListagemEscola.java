package olia.backend.api.domain.escola;

import olia.backend.api.domain.endereco.Endereco;

public record DadosListagemEscola(
    Long id, 
    String nome, 
    String nome_responsavel, 
    String email, 
    String telefone, 
    String cnpj, 
    String codigo_inep, 
    Endereco endereco, 
    String horario, 
    Double porcentagemOcupada
) {}
