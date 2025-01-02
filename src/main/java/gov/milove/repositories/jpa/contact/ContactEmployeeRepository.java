package gov.milove.repositories.jpa.contact;

import gov.milove.domain.user.ContactEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactEmployeeRepository extends JpaRepository<ContactEmployee, Long> {
}
