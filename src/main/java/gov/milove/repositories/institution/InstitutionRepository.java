package gov.milove.repositories.institution;

import gov.milove.domain.dto.InstitutionDto;
import gov.milove.domain.institution.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

//    Optional<Institution> findByTitle(String title);

    @Query("select new gov.milove.domain.dto.InstitutionDto(i.id, i.title, i.iconUrl) from Institution i")
    List<InstitutionDto> getAllAsDto();

//
//    @Transactional
//    @Modifying
//    @Query("update Institution i set i.title =?1 where i.id = ?2")
//    void updateTitleById(String new_title, Long id);
}
