package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.chat.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserChatRepo extends JpaRepository<UserChat, Long> {

}
