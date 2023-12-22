package gov.milove.repositories;

import gov.milove.domain.forum.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumUserRepo extends JpaRepository<ForumUser, Long> {
}
