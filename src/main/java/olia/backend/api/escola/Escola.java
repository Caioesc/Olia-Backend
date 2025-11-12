package olia.backend.api.escola;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import olia.backend.api.endereco.Endereco;

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
}
