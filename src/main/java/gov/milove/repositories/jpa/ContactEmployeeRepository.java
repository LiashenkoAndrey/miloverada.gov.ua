package gov.milove.repositories.jpa;

import gov.milove.domain.ContactEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactEmployeeRepository extends JpaRepository<ContactEmployee, Long> {
}
