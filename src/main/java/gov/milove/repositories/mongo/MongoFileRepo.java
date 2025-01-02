package gov.milove.repositories.mongo;

import gov.milove.domain.forum.MongoFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoFileRepo extends MongoRepository<MongoFile, String> {
}
