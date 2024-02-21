package gov.milove.services.impl;

import gov.milove.domain.DocumentGroup;
import gov.milove.repositories.document.DocumentGroupRepository;
import gov.milove.services.DocumentGroupService;
import gov.milove.services.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class DocumentGroupServiceImpl implements DocumentGroupService {

    private final DocumentGroupRepository documentGroupRepository;
    private final DocumentService documentService;

    @Override
    public void deleteById(Long id) {
       log.info("DELETE DOCUMENT GROUP - {}", id);

        DocumentGroup documentGroup = documentGroupRepository.findById(id).orElseThrow(EntityNotFoundException::new);
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

        documentGroupRepository.delete(documentGroup);
    }
}
