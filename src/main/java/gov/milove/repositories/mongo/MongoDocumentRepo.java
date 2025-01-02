package gov.milove.repositories.mongo;

import gov.milove.domain.mongo.MongoDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface MongoDocumentRepo extends MongoRepository<MongoDocument, String> {

    List<MongoDocument> findByFilename(String filename);

    void deleteByFilename(String filename);
}
