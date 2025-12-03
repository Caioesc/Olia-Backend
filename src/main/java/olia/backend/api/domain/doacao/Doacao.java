package olia.backend.api.domain.doacao;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import olia.backend.api.domain.escola.Escola;
import olia.backend.api.domain.usuario.Usuario;

@Entity(name = "Doacao")
@Table(name = "doacoes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Doacao {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private Double quantidade;
    
    private LocalDate data;
    
    @Enumerated(EnumType.STRING)
    private StatusDoacao status;

    // --- RELACIONAMENTOS ---

    @ManyToOne(fetch = FetchType.LAZY) // Carrega só quando precisa (performance)
    @JoinColumn(name = "escola_id")    // Nome da coluna no banco de dados
    private Escola escola;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Doacao(String codigo, Double quantidade, Escola escola, Usuario usuario) {
        this.codigo = codigo;
        this.quantidade = quantidade;
        this.escola = escola;
        this.usuario = usuario;
        this.data = LocalDate.now(); 
        this.status = StatusDoacao.PENDENTE; // Começa sempre como pendente
    }

    public void confirmar(Double quantidadeReal) {
    this.status = StatusDoacao.CONCLUIDO;
    if (quantidadeReal != null && quantidadeReal > 0) {
        this.quantidade = quantidadeReal; // Atualiza com o valor real medido
    }
}
}
