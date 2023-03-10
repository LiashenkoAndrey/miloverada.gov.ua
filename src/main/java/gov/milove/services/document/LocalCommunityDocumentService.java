package gov.milove.services.document;


import gov.milove.domain.Document;
import gov.milove.domain.SubGroup;
import gov.milove.repositories.document.DocumentRepository;
import gov.milove.repositories.document.DocumentSubGroupRepository;
import gov.milove.repositories.implementation.DocumentRepositoryMongo;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalCommunityDocumentService extends DocumentCrudService {

    private final DocumentSubGroupRepository subGroupRepository;
    private final DocumentRepositoryMongo documentRepositoryMongo;

    private final DocumentRepository documentRepository;

    public LocalCommunityDocumentService(DocumentRepositoryMongo documentRepositoryMongo, DocumentRepository documentRepository, DocumentSubGroupRepository subGroupRepository, DocumentRepositoryMongo documentRepositoryMongo1, DocumentRepository documentRepository1) {
        super(documentRepositoryMongo, documentRepository);
        this.subGroupRepository = subGroupRepository;
        this.documentRepositoryMongo = documentRepositoryMongo1;
        this.documentRepository = documentRepository1;
    }

    @Override
    public void createDocument(MultipartFile file, String title, Long subGroupId) {
        SubGroup subGroup = subGroupRepository.findById(subGroupId).orElseThrow(EntityExistsException::new);
        documentRepositoryMongo.saveToMongo(file);
        Document newDocument = Document.builder()
                .title(title)
                .document_filename(file.getOriginalFilename())
                .sub_group(subGroup)
                .build();

        documentRepository.save(newDocument);
        System.out.println("Document saved successfully! in LocalCommunityDocumentService");
    }
}
