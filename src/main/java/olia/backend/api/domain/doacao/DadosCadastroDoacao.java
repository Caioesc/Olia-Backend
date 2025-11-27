package olia.backend.api.domain.doacao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DadosCadastroDoacao(

    @NotBlank
    String codigo,
    
    @NotNull
    Double quantidade,
    
    @NotNull
    Long idEscola, 
    
    @NotNull
    Long idUsuario
) {}