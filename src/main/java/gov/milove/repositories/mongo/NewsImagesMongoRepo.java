package gov.milove.repositories.mongo;

import gov.milove.domain.MongoNewsImage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewsImagesMongoRepo extends MongoRepository<MongoNewsImage, String> {
}
