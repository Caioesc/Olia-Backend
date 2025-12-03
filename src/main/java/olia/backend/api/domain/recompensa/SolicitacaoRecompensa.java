package olia.backend.api.domain.recompensa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import olia.backend.api.domain.escola.Escola;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Table(name = "solicitacoes_recompensa")
@Entity(name = "SolicitacaoRecompensa")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class SolicitacaoRecompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Escola escola;

    @ManyToOne
    private Recompensa recompensa;

    private LocalDate data;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status;

    public SolicitacaoRecompensa(Escola escola, Recompensa recompensa) {
        this.escola = escola;
        this.recompensa = recompensa;
        this.data = LocalDate.now();
        this.status = StatusSolicitacao.PENDENTE;
    }

    public void aprovar() {
        this.status = StatusSolicitacao.APROVADO;
    }

    public void negar() {
        this.status = StatusSolicitacao.NEGADO;
    }
}
