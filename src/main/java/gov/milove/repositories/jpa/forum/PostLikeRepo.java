package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepo extends JpaRepository<PostLike, Long> {
}
