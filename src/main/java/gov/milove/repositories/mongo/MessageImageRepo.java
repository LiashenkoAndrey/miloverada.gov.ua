package gov.milove.repositories.mongo;

import gov.milove.domain.forum.mongo.MongoMessageImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageImageRepo extends MongoRepository<MongoMessageImage, String> {
}
