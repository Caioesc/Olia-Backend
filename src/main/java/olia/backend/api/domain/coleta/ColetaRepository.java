package olia.backend.api.domain.coleta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColetaRepository extends JpaRepository<Coleta, Long> {
    List<Coleta> findAllByStatus(StatusColeta status);

    @Query("SELECT COALESCE(SUM(c.quantidadeEstimada), 0) FROM Coleta c WHERE c.escola.id = :idEscola AND c.status = :status")
    Double somarTotalColetadoPorEscola(Long idEscola, StatusColeta status);
}
