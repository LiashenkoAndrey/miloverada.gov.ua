package gov.milove.repositories.document;

import gov.milove.domain.DocumentGroup;
import gov.milove.domain.dto.DocumentGroupDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DocumentGroupRepository extends JpaRepository<DocumentGroup, Long> {

    @Query("select new gov.milove.domain.dto.DocumentGroupDto(u.title, u.id) from DocumentGroup u where u.id =?1")
    DocumentGroupDto getDtoBySubGroupId(@Param("id") Long id);
}
