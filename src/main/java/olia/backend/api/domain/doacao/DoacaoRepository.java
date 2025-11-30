package olia.backend.api.domain.doacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    List<Doacao> findAllByUsuarioId(Long id);
    List<Doacao> findAllByEscolaId(Long id);
    java.util.Optional<Doacao> findByCodigo(String codigo);

    @Query("SELECT COALESCE(SUM(d.quantidade), 0) FROM Doacao d WHERE d.escola.id = :idEscola AND d.status = :status")
    Double somarTotalPorEscolaEStatus(Long idEscola, StatusDoacao status);

    // Contagem
    @Query("SELECT COUNT(d) FROM Doacao d WHERE d.escola.id = :idEscola AND d.status = :status")
    Integer contarPorEscolaEStatus(Long idEscola, StatusDoacao status);

}
