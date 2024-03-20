package gov.milove.repositories.forum;

import gov.milove.domain.dto.forum.ChatDto;
import gov.milove.domain.forum.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserChatRepo extends JpaRepository<UserChat, Long> {

    @Query("select c from Chat c join UserChat cv on c.id = cv.chat.id where cv.userId = :id order by cv.lastVisitedOn desc ")
    List<ChatDto> getUserChatsId(@Param("id") String userId);
}
