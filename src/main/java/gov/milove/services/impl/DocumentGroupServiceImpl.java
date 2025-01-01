package gov.milove.services.impl;

import gov.milove.domain.document.DocumentGroup;
import gov.milove.repositories.jpa.document.DocumentGroupRepo;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.document.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class DocumentGroupServiceImpl implements DocumentGroupService {

    private final DocumentGroupRepo documentGroupRepo;
    private final DocumentService documentService;

    @Override
    public void deleteById(Long id) {
       log.info("DELETE DOCUMENT GROUP - {}", id);

        DocumentGroup documentGroup = documentGroupRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        deleteGroup(documentGroup);
    }

    public void deleteGroup(DocumentGroup documentGroup) {
        if (!documentGroup.getDocuments().isEmpty()) {
            documentService.deleteAll(documentGroup.getDocuments());
        }
        if (!documentGroup.getGroups().isEmpty()) {
            for (DocumentGroup group : documentGroup.getGroups()) {
                deleteGroup(group);
            }
        }

        documentGroupRepo.delete(documentGroup);
    }
}
