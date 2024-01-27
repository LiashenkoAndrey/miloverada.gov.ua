package gov.milove.repositories.forum;

import gov.milove.domain.dto.forum.ChatMetadataDto;
import gov.milove.domain.forum.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {

    @Query(value = "select  * from forum.chat c inner join topic_chats ct on ct.topic_id = :topicId  where ct.chats_id = c.id", nativeQuery = true)
    List<Chat> findByTopicId(@Param("topicId") Long topicId);

    @Query(value = "select last_read_message_id as last_read_message_id, unread_messages_count as unread_messages_count from forum.get_chat_metadata(CAST(:chatId as integer) , CAST(:userId as text));", nativeQuery = true)
    ChatMetadataDto getChatMetadata(@Param("chatId") Long chatId, @Param("userId") String userId);


    @Transactional
    @Modifying
    @Query(value = "delete from forum.unread_messages where message_id = :messageId", nativeQuery = true)
    void deleteFromLastReadMessagesTable(@Param("messageId") Long messageId);
}
