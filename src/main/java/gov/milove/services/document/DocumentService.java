package gov.milove.services.document;

import gov.milove.domain.document.Document;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    Document saveDocument(Long groupId, MultipartFile file, String mame);

    void deleteAll(List<Document> documents);

    void delete(Document document);
}
