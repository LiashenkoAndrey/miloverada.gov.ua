package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.UserChat;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepo extends JpaRepository<UserChat, Long> {

    Optional<UserChat> findByUserIdAndChatId(String userId, Long chatId);
}
