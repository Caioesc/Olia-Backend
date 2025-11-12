package olia.backend.api.usuario;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import olia.backend.api.endereco.Endereco;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    private String cpf;
    private String telefone;
    private String numeroNis;
    private boolean temBolsaFamilia;

    @Embedded
    private Endereco endereco;

    private boolean ativo;

    public Usuario(DadosCadastroUsuario dados){
        this.ativo = true;
        this.nome = dados.nome();
        this.email = dados.email();
        this.senha = dados.senha();
        this.cpf = dados.cpf();
        this.telefone = dados.telefone();
        this.temBolsaFamilia = dados.temBolsaFamilia();
        this.numeroNis = dados.numeroNis();
        this.endereco = new Endereco(dados.endereco());
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoUsuario dados) {
        if (dados.nome() != null){
            this.nome = dados.nome();
        }
        if (dados.telefone() != null){
            this.telefone = dados.telefone();
        }
        if (dados.endereco() != null){
            this.endereco.atualizarInformacoes(dados.endereco());
        }
        if (dados.email() != null){
            this.email = dados.email();
        }


    }

}
