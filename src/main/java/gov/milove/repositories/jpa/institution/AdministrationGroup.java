package gov.milove.repositories.jpa.institution;

import gov.milove.domain.dto.AdministrationGroupDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface AdministrationGroup extends JpaRepository<gov.milove.domain.administration.AdministrationGroup, Long> {

    @Query("from AdministrationGroup g where g.administration_group is null")
    List<gov.milove.domain.administration.AdministrationGroup> findAllWhereGroupIdIsNull();

    @Transactional
    @Modifying
    @Query("delete from AdministrationGroup g where g.id =?1")
    void deleteGroupById(Long id);

    @Query("select new gov.milove.domain.dto.AdministrationGroupDto(g.id, g.title) from AdministrationGroup g where g.id =?1")
    Optional<AdministrationGroupDto> findDtoById(Long id);
}
