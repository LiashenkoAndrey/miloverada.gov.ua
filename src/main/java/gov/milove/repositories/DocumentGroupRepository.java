package gov.milove.repositories;

import gov.milove.domain.DocumentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentGroupRepository extends JpaRepository<DocumentGroup, Long> {
}
