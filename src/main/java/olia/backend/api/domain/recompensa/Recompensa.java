package olia.backend.api.domain.recompensa;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "recompensas")
@Entity(name = "recompensa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recompensa {

    @Id
    @Column(length = 36)
    private String id;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(name = "pontos_necessarios")
    private Integer pontosNecessarios;

    @Column(name = "url_imagem", length = 255)
    private String urlImagem;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @OneToMany(mappedBy = "recompensa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SolicitacaoRecompensa> solicitacoes;

    @PrePersist
    public void aoCriar() {
        this.criadoEm = LocalDateTime.now();
        this.atualizadoEm = LocalDateTime.now();
        if (this.id == null)
            this.id = java.util.UUID.randomUUID().toString();
    }

    @PreUpdate
    public void aoAtualizar() {
        this.atualizadoEm = LocalDateTime.now();
    }
}
