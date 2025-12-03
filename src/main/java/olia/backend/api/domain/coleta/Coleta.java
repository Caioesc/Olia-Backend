package olia.backend.api.domain.coleta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import olia.backend.api.domain.escola.Escola;

import java.time.LocalDate;

@Table(name = "coletas")
@Entity(name = "Coleta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Coleta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double quantidadeEstimada;
    private LocalDate dataPreferida;

    @Enumerated(EnumType.STRING)
    private StatusColeta status;

    @ManyToOne // Várias coletas podem ser da mesma escola
    private Escola escola;

    public Coleta(DadosSolicitacaoColeta dados, Escola escola) {
        this.quantidadeEstimada = dados.quantidade();
        this.dataPreferida = dados.data();
        this.escola = escola;
        this.status = StatusColeta.PENDENTE; // Começa sempre como pendente
    }

    public void agendar() {
        this.status = StatusColeta.AGENDADA;
    }

    public void concluir() {
        this.status = StatusColeta.CONCLUIDA;
    }
}