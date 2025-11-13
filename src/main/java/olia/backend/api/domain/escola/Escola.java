package olia.backend.api.domain.escola;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import olia.backend.api.domain.endereco.Endereco;

@Table(name = "escolas")
@Entity(name = "Escola")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Escola {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cnpj;
    private String codigo_inep;
    private String telefone;
    private String email;
    private String email_acesso;
    private String senha;
    private String nome_responsavel;

    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private Capacidade capacidade;

    private boolean ativo;

    public Escola(DadosCadastroEscola dados){
        this.ativo = true;
        this.nome = dados.nome();
        this.cnpj = dados.cnpj();
        this.codigo_inep = dados.codigo_inep();
        this.telefone = dados.telefone();
        this.email = dados.email();
        this.email_acesso = dados.email_acesso();
        this.senha = dados.senha();
        this.nome_responsavel = dados.nome_responsavel();
        this.endereco = new Endereco(dados.endereco());
        this.capacidade = dados.capacidade();
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoEscola dados) {
        if (dados.nome() != null){
            this.nome = dados.nome();
        }
        if (dados.telefone() != null){
            this.telefone = dados.telefone();
        }
        if (dados.email() != null){
            this.email = dados.email();
        }
        if (dados.email_acesso() != null){
            this.email_acesso = dados.email_acesso();
        }
        if (dados.senha() != null){
            this.senha = dados.senha();
        }
        if (dados.nome_responsavel() != null){
            this.nome_responsavel = dados.nome_responsavel();
        }
        if (dados.endereco() != null){
            this.endereco.atualizarInformacoes(dados.endereco());
        }
        if (dados.capacidade() != null){
            this.capacidade = dados.capacidade();
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
