package olia.backend.api.domain.recompensa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "recompensas")
@Entity(name = "Recompensa")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Recompensa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private Integer custo;
    private String icone; // Caminho da imagem (ex: assets/...)
    private boolean ativo;

    public Recompensa(DadosCadastroRecompensa dados) {
        this.titulo = dados.titulo();
        this.custo = dados.custo();
        this.icone = dados.icone();
        this.ativo = true;
    }

    public void atualizar(DadosCadastroRecompensa dados) {
        if (dados.titulo() != null)
            this.titulo = dados.titulo();
        if (dados.custo() != null)
            this.custo = dados.custo();
        if (dados.icone() != null)
            this.icone = dados.icone();
    }
}
