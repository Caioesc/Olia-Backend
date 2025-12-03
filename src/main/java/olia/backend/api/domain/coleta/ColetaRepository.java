package olia.backend.api.domain.coleta;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ColetaRepository extends JpaRepository<Coleta, Long> {
    List<Coleta> findAllByStatus(StatusColeta status);
}
