package gov.milove.repositories.mongo;

import gov.milove.domain.MongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface MongoDocumentRepo extends MongoRepository<MongoDocument, String> {

    List<MongoDocument> findByFilename(String filename);

    void deleteByFilename(String filename);
}
