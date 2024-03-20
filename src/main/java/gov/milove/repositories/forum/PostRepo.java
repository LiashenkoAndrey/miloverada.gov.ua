package gov.milove.repositories.forum;

import gov.milove.domain.forum.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepo extends JpaRepository<Post, Long> {
}
