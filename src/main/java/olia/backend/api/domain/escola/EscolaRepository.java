package olia.backend.api.domain.escola;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EscolaRepository extends JpaRepository<Escola, Long> {
    Page<Escola> findAllByAtivoTrue(Pageable paginacao);

    @Query("SELECT e FROM Escola e WHERE e.email_acesso = :emailAcesso")
    Escola buscarPorEmailDeAcesso(String emailAcesso);
}
