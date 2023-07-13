package gov.milove.services.document;

import gov.milove.domain.Document;
import gov.milove.domain.administration.AdministrationGroup;
import gov.milove.repositories.administration.AdministrationGroupRepository;
import gov.milove.repositories.document.DocumentRepository;
import gov.milove.repositories.impl.DocumentRepositoryMongo;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdministrationDocumentService extends DocumentCrudService {

    private final AdministrationGroupRepository groupRepository;

    private final DocumentRepository documentRepository;

    private final DocumentRepositoryMongo documentRepositoryMongo;

    public AdministrationDocumentService(DocumentRepositoryMongo documentRepositoryMongo, DocumentRepository documentRepository, AdministrationGroupRepository groupRepository, DocumentRepository documentRepository1, DocumentRepositoryMongo documentRepositoryMongo1) {
        super(documentRepositoryMongo, documentRepository);
        this.groupRepository = groupRepository;
        this.documentRepository = documentRepository1;
        this.documentRepositoryMongo = documentRepositoryMongo1;
    }

    @Override
    public void createDocument(MultipartFile file, String title, Long groupId) {
        AdministrationGroup group = groupRepository.findById(groupId).orElseThrow(EntityExistsException::new);
        documentRepositoryMongo.saveToMongo(file);
        Document newDocument = Document.builder()
                .title(title)
                .document_filename(file.getOriginalFilename())
                .administration_group(group)
                .build();

        documentRepository.save(newDocument);
    }
}
