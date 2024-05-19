package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepo extends JpaRepository<File, Long> {
    Optional<File> findFirstByHashCode(String hashCode);
}
