package gov.milove.repositories;

import gov.milove.domain.About;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AboutRepo extends JpaRepository<About, Long> {

}
