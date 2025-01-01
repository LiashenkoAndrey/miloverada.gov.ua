package gov.milove.repositories.jpa.institution;

import gov.milove.domain.administration.AdministrationEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdministrationEmployeeRepository extends JpaRepository<AdministrationEmployee, Long> {

    @Query("from AdministrationEmployee e where e.administration_group is null")
    List<AdministrationEmployee> findAllWhereGroupIdIsNull();
}
