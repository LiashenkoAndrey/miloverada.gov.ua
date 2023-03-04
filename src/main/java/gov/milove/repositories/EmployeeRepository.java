package gov.milove.repositories;

import gov.milove.domain.intitution.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("from Employee e where e.institution.title =?1 order by e.sub_institution")
    List<Employee> findByInstitution_Title(String title);
}
