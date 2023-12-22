package gov.milove.repositories;

import gov.milove.domain.forum.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {

    List<Chat> findByTopicId(Long topicId);


}
