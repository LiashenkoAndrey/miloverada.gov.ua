package gov.milove.repositories.jpa;

import gov.milove.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, String> {
}
