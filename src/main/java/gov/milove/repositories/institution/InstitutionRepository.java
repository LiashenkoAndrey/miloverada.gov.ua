package gov.milove.repositories.institution;

import gov.milove.domain.institution.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    Optional<Institution> findByTitle(String title);

    @Transactional
    @Modifying
    @Query("update Institution i set i.title =?1 where i.id = ?2")
    void updateTitleById(String new_title, Long id);
}
