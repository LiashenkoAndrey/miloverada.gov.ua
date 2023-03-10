package gov.milove.services.document;

import gov.milove.domain.Document;
import gov.milove.exceptions.DocumentCrudServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DocumentService {

    public void createDocument(MultipartFile file, String title, Long groupId) throws DocumentCrudServiceException;

    public byte[] getDocumentBinaryById(String id);

    public Optional<Document> getDocumentById(Long id);

    public void updateDocument(String document_id, MultipartFile file, String title) throws DocumentCrudServiceException;

    public void deleteDocumentByFilename(String id) throws DocumentCrudServiceException;

    public void deleteAll(List<Document> documents) throws DocumentCrudServiceException;

}
