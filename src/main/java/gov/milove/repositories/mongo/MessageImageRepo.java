package gov.milove.repositories.mongo;

import gov.milove.domain.forum.MongoMessageImage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface MessageImageRepo extends MongoRepository<MongoMessageImage, String> {
}
