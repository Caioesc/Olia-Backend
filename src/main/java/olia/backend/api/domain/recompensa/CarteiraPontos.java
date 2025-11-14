package olia.backend.api.domain.recompensa;

import jakarta.persistence.*;
import lombok.*;
import olia.backend.api.domain.usuario.Usuario;

import java.time.LocalDateTime;

@Table(name = "carteira_pontos")
@Entity(name = "carteira_ponto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarteiraPontos {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private Integer pontos;

    @Column(length = 50)
    private String tipo;
    // ganho, gasto, bonus, ajuste...

    @Column(length = 500)
    private String descricao;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm;

    @PrePersist
    public void aoCriar() {
        this.criadoEm = LocalDateTime.now();
        if (this.id == null)
            this.id = java.util.UUID.randomUUID().toString();

    }
}
