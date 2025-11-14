package olia.backend.api.domain.recompensa;

import jakarta.persistence.*;
import lombok.*;
import olia.backend.api.domain.usuario.Usuario;

import java.time.LocalDateTime;

@Table(name = "solicitacoes_recompensas")
@Entity(name = "solicitacoes_recompensa")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SolicitacaoRecompensa {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recompensa_id", nullable = false)
    private Recompensa recompensa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 20)
    private String status;
    // pendente, aprovado, rejeitado

    @Column(name = "solicitado_em")
    private LocalDateTime solicitadoEm;

    @Column(name = "processado_em")
    private LocalDateTime processadoEm;

    @Column(length = 500)
    private String observacoes;

    @PrePersist
    public void aoCriar() {
        this.solicitadoEm = LocalDateTime.now();
        if (this.id == null)
            this.id = java.util.UUID.randomUUID().toString();
    }

}
