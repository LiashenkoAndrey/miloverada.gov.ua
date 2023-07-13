package gov.milove.services.document;

import gov.milove.domain.Document;
import gov.milove.exceptions.DocumentCrudServiceException;
import gov.milove.repositories.document.DocumentRepository;
import gov.milove.repositories.impl.DocumentRepositoryMongo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public abstract class DocumentCrudService implements DocumentService {

    private final DocumentRepositoryMongo documentRepositoryMongo;
    private final DocumentRepository documentRepository;


    public DocumentCrudService(DocumentRepositoryMongo documentRepositoryMongo, DocumentRepository documentRepository) {
        this.documentRepositoryMongo = documentRepositoryMongo;
        this.documentRepository = documentRepository;
    }

    public abstract void createDocument(MultipartFile file, String title, Long subGroupId) ;

    public void deleteDocumentByFilename(String id) throws DocumentCrudServiceException {
        try {
            documentRepository.deleteByDocument_filename(id);
            List<String> list = new ArrayList<>();
            list.add(id);
            documentRepositoryMongo.deleteDocumentsById(list);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DocumentCrudServiceException(ex.getMessage());
        }
    }

    @Override
    public void deleteAll(List<Document> documents) throws DocumentCrudServiceException {
        try {
            for (Document d : documents) {
                deleteDocumentByFilename(d.getDocument_filename());
            }
        } catch (Exception ex) {
            throw new DocumentCrudServiceException(ex.getMessage());
        }
    }


    public byte[] getDocumentBinaryById(String id) {
        return documentRepositoryMongo.getFromMongoById(id);
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);
    }

    public void updateDocument(String document_id, MultipartFile file, String title) throws DocumentCrudServiceException {
        try {
            Document document = documentRepository.findByDocument_filename(document_id).orElseThrow(EntityNotFoundException::new);
            if (file != null) {
                documentRepositoryMongo.updateDocument(document_id, file);
                document.setDocument_filename(file.getOriginalFilename());
            }
            if (title != null) {
                if (!title.equals("")) document.setTitle(title);
            }
            documentRepository.save(document);
        } catch (Exception ex) {
            throw new DocumentCrudServiceException(ex.getMessage());
        }

    }
}
