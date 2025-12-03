package olia.backend.api.domain.escola;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

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
public class Escola implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cnpj;
    private String codigo_inep;
    private String telefone;
    private String email;
    private String email_acesso;
    private String senha;
    private String nome_responsavel;
    private String horario;
    private Integer metaAtual;

    @Embedded
    private Endereco endereco;

    @Enumerated(EnumType.STRING)
    private Capacidade capacidade;

    private boolean ativo;

    public Escola(DadosCadastroEscola dados) {
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
        this.horario = dados.horario();
        this.metaAtual = switch (this.capacidade) {
            case PEQUENA -> 500; // Até 50L
            case MEDIA -> 1000; // Até 100L
            case GRANDE -> 2000; // Acima de 200L
        };
    }

    // Método chamado quando a escola resgata uma recompensa
    public void aumentarMetaAposResgate() {
        if (this.metaAtual == null) {
            this.metaAtual = 500; // Valor padrão de segurança
        }
        // Aumenta a meta em 50% e converte para inteiro
        this.metaAtual = (int) (this.metaAtual * 1.5);
    }

    public void criptografarSenha(PasswordEncoder passwordEncoder) {
        this.senha = passwordEncoder.encode(this.senha);
    }

    public void atualizarInformacoes(@Valid DadosAtualizacaoEscola dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.telefone() != null) {
            this.telefone = dados.telefone();
        }
        if (dados.email() != null) {
            this.email = dados.email();
        }
        if (dados.email_acesso() != null) {
            this.email_acesso = dados.email_acesso();
        }
        if (dados.senha() != null) {
            this.senha = dados.senha();
        }
        if (dados.nome_responsavel() != null) {
            this.nome_responsavel = dados.nome_responsavel();
        }
        if (dados.endereco() != null) {
            this.endereco.atualizarInformacoes(dados.endereco());
        }
        if (dados.capacidade() != null) {
            this.capacidade = dados.capacidade();
        }
    }

    public void excluir() {
        this.ativo = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ESCOLA"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email_acesso; // Usamos o email_acesso para login
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    } // Só loga se estiver ativa
}
