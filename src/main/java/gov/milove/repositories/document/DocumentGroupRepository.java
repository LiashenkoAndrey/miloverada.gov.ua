package gov.milove.repositories.document;

import gov.milove.domain.DocumentGroup;
import gov.milove.domain.dto.DocumentGroupDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentGroupRepository extends JpaRepository<DocumentGroup, Long> {

    @Query("select new gov.milove.domain.dto.DocumentGroupDto(u.title, u.id) from DocumentGroup u where u.id =?1")
    DocumentGroupDto getDtoBySubGroupId(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("update DocumentGroup u set u.title =?1 where u.id = ?2 ")
    void updateTitle(@Param("title") String title, @Param("id") Long id);

    @Query("select new gov.milove.domain.dto.DocumentGroupDto(g.title, g.id) from DocumentGroup g order by g.is_general desc")
    List<DocumentGroupDto> findGeneralGroupsDto();
}
