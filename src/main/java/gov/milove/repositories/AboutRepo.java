package gov.milove.repositories;

import gov.milove.domain.About;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AboutRepo extends MongoRepository<About, String> {

}
