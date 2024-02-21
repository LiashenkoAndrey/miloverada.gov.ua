package gov.milove.repositories.document;

import gov.milove.domain.DocumentGroup;
import gov.milove.domain.dto.DocumentGroupWithGroupsDto;
import gov.milove.domain.dto.DocumentGroupWithGroupsDtoAndDocumentsDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DocumentGroupRepository extends JpaRepository<DocumentGroup, Long> {

    Optional<DocumentGroupWithGroupsDtoAndDocumentsDto> findDistinctById(Long id);

    List<DocumentGroupWithGroupsDto> findDistinctByDocumentGroupIdOrderByCreatedOn(Long id);
}
