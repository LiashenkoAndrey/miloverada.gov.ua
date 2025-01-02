package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.chat.PrivateChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PrivateChatRepo  extends JpaRepository<PrivateChat, Long> {

    @Query(value = "select * from forum.private_chat p where p.user1_id = :user1_id and p.user2_id = :user2_id or p.user1_id = :user2_id and p.user2_id = :user1_id", nativeQuery = true)
    Optional<PrivateChat> findPrivateChatBetweenToUsers(@Param("user1_id") String user1_id, @Param("user2_id") String user2_id);
}
