package gov.milove.repositories.document;

import gov.milove.domain.Document;
import gov.milove.domain.dto.DocumentWithGroupDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    Optional<Document> findByHashCode(Integer hashCode);

    @Query("select count(d.id) > 1 from Document d  where d.name = :name")
    boolean documentUsedMoreThenOneTime(@Param("name") String name);

    List<DocumentWithGroupDto> searchDistinctByNameContainingIgnoreCaseOrTitleContainingIgnoreCase(String title, String name);
}
