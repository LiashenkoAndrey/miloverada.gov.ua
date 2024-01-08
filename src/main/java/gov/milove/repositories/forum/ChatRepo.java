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

    List<Chat> findByTopicId(Long topicId);

    @Query(value = "select last_read_message_id as last_read_message_id, unread_messages_count as unread_messages_count from forum.get_chat_metadata(CAST(:chatId as integer) , CAST(:userId as text));", nativeQuery = true)
    ChatMetadataDto getChatMetadata(@Param("chatId") Long chatId, @Param("userId") String userId);


    @Query(value = "select u.message_id from forum.unread_messages u where u.chat_id = :chatId and u.user_id = :userId", nativeQuery = true)
    Long getLastReadMessageIdOfChat(@Param("chatId") Long chatId, @Param("userId") String userId);


    @Transactional
    @Modifying
    @Query(value = "delete from forum.unread_messages where message_id = :messageId", nativeQuery = true)
    void deleteFromLastReadMessagesTable(@Param("messageId") Long messageId);
}
