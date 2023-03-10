package gov.milove.repositories.institution;

import gov.milove.domain.institution.Institution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findByTitle(String title);
}
