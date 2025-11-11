package olia.backend.api.usuario;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import olia.backend.api.endereco.DadosEndereco;
import olia.backend.api.endereco.Endereco;

public record DadosCadastroUsuario(

        @NotBlank   //Verifica se não é nulo e nem vazio
        String nome,

        @NotBlank
        @Email  //Verifica se está no formato de email
        String email,

        @NotBlank
        String senha,

        @NotBlank
        @Pattern(regexp = "\\d{11}") //Verifica se é um dígito com 11 números
        String cpf,

        @NotBlank
        String telefone,

        String numeroNis,

        @NotNull //NotBlank é apenas para campos String
        boolean temBolsaFamilia,

        //Valid é para verificar o DTO DadosEndereco que está dentro do DTO DadosCadastroUsuario
        @NotNull @Valid DadosEndereco endereco) {
}