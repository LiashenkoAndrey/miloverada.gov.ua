package gov.milove.repositories.jpa.document;

import gov.milove.domain.document.DocumentGroup;
import gov.milove.domain.dto.document.DocumentGroupWithGroupsDto;
import gov.milove.domain.dto.document.DocumentGroupWithGroupsAndDocumentsDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentGroupRepo extends JpaRepository<DocumentGroup, Long> {

    Optional<DocumentGroupWithGroupsAndDocumentsDto> findDistinctById(Long id);

    List<DocumentGroupWithGroupsDto> findDistinctByDocumentGroupIdOrderByCreatedOn(Long id);
}
