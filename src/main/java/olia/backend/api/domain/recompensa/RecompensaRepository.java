package olia.backend.api.domain.recompensa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecompensaRepository extends JpaRepository<Recompensa, Long> {
    List<Recompensa> findAllByAtivoTrue(); // Só lista o que está ativo na aba de recompensas
}
