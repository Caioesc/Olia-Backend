package olia.backend.api.domain.recompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SolicitacaoRecompensaRepository extends JpaRepository<SolicitacaoRecompensa, Long> {
    // Lista os pendentes primeiro
    List<SolicitacaoRecompensa> findAllByOrderByStatusAscDataDesc();

    // Soma o custo de todas as recompensas pedidas pela escola (excluindo as negadas)
    @Query("""
        SELECT COALESCE(SUM(r.custo), 0) 
        FROM SolicitacaoRecompensa s 
        JOIN s.recompensa r 
        WHERE s.escola.id = :idEscola AND s.status != 'NEGADO'
    """)
    Integer somarPontosGastos(Long idEscola);
}
