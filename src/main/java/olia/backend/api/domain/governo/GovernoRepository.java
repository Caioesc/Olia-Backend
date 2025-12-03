package olia.backend.api.domain.governo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface GovernoRepository extends JpaRepository<Governo, Long> {
    UserDetails findByEmail(String email);
}
