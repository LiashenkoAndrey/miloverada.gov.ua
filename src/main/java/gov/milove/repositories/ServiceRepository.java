package gov.milove.repositories;

import gov.milove.domain.digitalQueue.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
