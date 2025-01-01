package gov.milove.repositories.jpa.forum;

import gov.milove.domain.dto.forum.ChatDto;
import gov.milove.domain.dto.forum.ChatDtoWithMetadata;
import gov.milove.domain.dto.forum.ChatMetadataDto;
import gov.milove.domain.forum.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {

    @Query(value = "select  * from forum.chat c inner join topic_chats ct on ct.topic_id = :topicId  where ct.chats_id = c.id order by c.created_on desc", nativeQuery = true)
    List<ChatDto> findByTopicId(@Param("topicId") Long topicId);

    @Query("select chat from Chat chat where chat.id = :id")
    ChatDto findChatById(@Param("id") Long id);

    @Query(value = "select last_read_message_id as last_read_message_id, unread_messages_count as unread_messages_count, is_member as is_member from forum.get_chat_metadata(CAST(:chatId as integer) , CAST(:userId as text));", nativeQuery = true)
    ChatMetadataDto getChatMetadata(@Param("chatId") Long chatId, @Param("userId") String userId);


    @Query(value = """
            select c, function('forum.get_chat_metadata', CAST(c.id as integer) , str('google-oauth2|106434411070973160536') ) from Chat c\s
            join UserChat uc on c.id = uc.chat.id where uc.userId = 'google-oauth2|106434411070973160536'
            """)
    ChatDtoWithMetadata getChatWithMeta(@Param("chatId") Long chatId, @Param("userId") String userId);

    @Transactional
    @Modifying
    @Query(value = "delete from forum.unread_messages where message_id = :messageId", nativeQuery = true)
    void deleteFromLastReadMessagesTable(@Param("messageId") Long messageId);
}
