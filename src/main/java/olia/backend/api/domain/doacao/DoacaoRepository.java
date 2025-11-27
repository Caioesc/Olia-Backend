package olia.backend.api.domain.doacao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {

    List<Doacao> findAllByUsuarioId(Long id);

    //soma o óleo recebido por uma escola específica
    @Query("SELECT COALESCE(SUM(d.quantidade), 0) FROM Doacao d WHERE d.escola.id = :idEscola")
    Double totalDoadoPorEscola(Long idEscola);
}
