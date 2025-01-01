package gov.milove.repositories.mongo;

import gov.milove.domain.media.About;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface AboutRepo extends MongoRepository<About, String> {

}
