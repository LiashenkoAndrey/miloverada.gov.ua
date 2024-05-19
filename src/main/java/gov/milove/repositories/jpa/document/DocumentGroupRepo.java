package gov.milove.repositories.jpa.document;

import gov.milove.domain.DocumentGroup;
import gov.milove.domain.dto.DocumentGroupWithGroupsDto;
import gov.milove.domain.dto.DocumentGroupWithGroupsDtoAndDocumentsDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DocumentGroupRepo extends JpaRepository<DocumentGroup, Long> {

    Optional<DocumentGroupWithGroupsDtoAndDocumentsDto> findDistinctById(Long id);

    List<DocumentGroupWithGroupsDto> findDistinctByDocumentGroupIdOrderByCreatedOn(Long id);
}
