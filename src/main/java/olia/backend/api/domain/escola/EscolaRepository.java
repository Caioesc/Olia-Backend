package olia.backend.api.domain.escola;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EscolaRepository extends JpaRepository<Escola, Long> {
    Page<Escola> findAllByAtivoTrue(Pageable paginacao);

    @Query("SELECT e FROM Escola e WHERE e.email_acesso = :emailAcesso")
    Escola buscarPorEmailDeAcesso(String emailAcesso);

    @Query("""
        SELECT e.id, e.nome, COALESCE(SUM(d.quantidade), 0)
        FROM Escola e
        LEFT JOIN Doacao d ON e.id = d.escola.id
        WHERE e.ativo = true
        GROUP BY e.id, e.nome
        ORDER BY SUM(d.quantidade) DESC
    """)
    List<Object[]> buscarRankingBruto();
}
