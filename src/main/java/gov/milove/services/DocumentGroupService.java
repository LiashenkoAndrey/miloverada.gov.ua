package gov.milove.services;

import gov.milove.domain.DocumentGroup;
import gov.milove.repositories.DocumentGroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DocumentGroupService {

    private final DocumentGroupRepository documentGroupRepository;

    public DocumentGroupService(DocumentGroupRepository documentGroupRepository) {
        this.documentGroupRepository = documentGroupRepository;
    }

    public void createGroup(String title) {
        DocumentGroup group = new DocumentGroup();
        group.setTitle(title);

        documentGroupRepository.save(group);
    }

    public List<DocumentGroup> findAll() {
        return documentGroupRepository.findAll();
    }

    public Optional<DocumentGroup> findDocumentGroupById(Long id) {
        return documentGroupRepository.findById(id);
    }

    public boolean deleteGroup(Long id) {
        boolean success;
        try {
            documentGroupRepository.deleteById(id);
            success = true;
        } catch (Exception ex) {
            success = false;
            throw new RuntimeException(ex);
        }
        return success;
    }
}
