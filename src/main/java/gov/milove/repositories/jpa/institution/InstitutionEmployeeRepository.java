package gov.milove.repositories.jpa.institution;

import gov.milove.domain.institution.InstitutionEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionEmployeeRepository extends JpaRepository<InstitutionEmployee, Long> {

//    @Query("from InstitutionEmployee e where e.institution.title =?1 order by e.sub_institution")
//    List<InstitutionEmployee> findByInstitution_Title(String title);
}
