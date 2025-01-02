package gov.milove.services;

import gov.milove.domain.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    Document saveDocument(Long groupId, MultipartFile file, String mame);

    void deleteAll(List<Document> documents);

    void delete(Document document);
}
