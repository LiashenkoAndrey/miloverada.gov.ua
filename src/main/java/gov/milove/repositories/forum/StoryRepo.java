package gov.milove.repositories.forum;

import gov.milove.domain.forum.Story;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepo extends JpaRepository<Story, Long> {
}
