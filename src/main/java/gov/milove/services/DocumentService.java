package gov.milove.services;


import gov.milove.domain.Document;
import gov.milove.domain.SubGroup;
import gov.milove.repositories.DocumentRepository;
import gov.milove.repositories.DocumentRepositoryMongo;
import gov.milove.repositories.DocumentSubGroupRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class DocumentService {

    private final DocumentRepositoryMongo documentRepositoryMongo;
    private final DocumentRepository documentRepository;
    private final DocumentSubGroupRepository subGroupRepository;

    public DocumentService(DocumentRepositoryMongo documentRepositoryMongo, DocumentRepository documentRepository, DocumentSubGroupRepository subGroupRepository) {
        this.documentRepositoryMongo = documentRepositoryMongo;
        this.documentRepository = documentRepository;
        this.subGroupRepository = subGroupRepository;
    }

    public void createDocument(MultipartFile file, String title, Long subGroupId) {
        SubGroup subGroup = subGroupRepository.findById(subGroupId).orElseThrow(EntityExistsException::new);
        documentRepositoryMongo.saveToMongo(file);
        Document newDocument = Document.builder()
                .title(title)
                .document_filename(file.getOriginalFilename())
                .sub_group(subGroup)
                .build();

        documentRepository.save(newDocument);
        System.out.println("Document saved successfully!");
    }

    public boolean deleteDocument(String id) {
        boolean success;
        try {
            documentRepository.deleteByDocument_id(id);
            List<String> list = new ArrayList<>();
            list.add(id);
            documentRepositoryMongo.deleteDocumentsById(list);
            success = true;
        } catch (Exception ex) {
            success = false;
            throw new RuntimeException(ex);
        }
        return success;
    }

    public byte[] getDocumentById(String id) {
        return documentRepositoryMongo.getFromMongoById(id);
    }

    public void updateDocument(String document_id, MultipartFile file, String title) {
        Document document = documentRepository.findByDocument_filename(document_id).orElseThrow(EntityExistsException::new);
        if (file != null) {
            documentRepositoryMongo.updateDocument(document_id, file);
            document.setDocument_filename(file.getOriginalFilename());
        }
        System.out.println("upd doc TITLE: " + title);
        if (title != null) document.setTitle(title);
        documentRepository.save(document);
    }
}
