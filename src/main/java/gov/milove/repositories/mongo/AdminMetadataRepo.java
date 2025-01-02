package gov.milove.repositories.mongo;

import gov.milove.domain.AdminMetadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AdminMetadataRepo extends MongoRepository<AdminMetadata, String> {
}
