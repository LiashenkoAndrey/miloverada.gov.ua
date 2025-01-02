package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.message.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findAllByChatId(Long id, Pageable pageable);

    @Query("from Message m where m.id < :fromMessageId and m.chatId = :chatId order by m.createdOn desc limit 20")
    List<Message> getPrevious(@Param("fromMessageId") Long fromMessageId, @Param("chatId") Long chatId);


    @Query("from Message m where m.id > :fromMessageId and m.chatId = :chatId order by m.createdOn")
    List<Message> getNext(@Param("fromMessageId") Long fromMessageId, @Param("chatId") Long chatId);

    @Modifying
    @Transactional
    @Query("delete from MessageImage m where m.message_id = :messageId and m.imageId = :imageId")
    void deleteMessageImage(@Param("imageId") String imageId, @Param("messageId") Long messageId);

    @Query(value = "select count(id) > 1 from forum.message_image img  where image_id = :imageId", nativeQuery = true)
    boolean messageImageIsUsedMoreThenOneTime(@Param("imageId") String imageId);


    @Query(value = "select * from forum.message m where m.chat_id = :chatId and m.id >= :messageId order by m.created_on;", nativeQuery = true)
    List<Message> getNewPageOfMessages(@Param("chatId") Long chatId, @Param("messageId") Long messageId, Pageable pageable);



    @Transactional
    @Modifying
    @Query(value = "update forum.unread_messages set message_id = :messageId where chat_id = :chatId and user_id = :userId", nativeQuery = true)
    void updateLastReadMessage(@Param("chatId") Long chatId, @Param("userId") String userId, @Param("messageId") Long messageId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into forum.unread_messages (chat_id, user_id, message_id) VALUES (:chatId, :userId, :messageId)")
    void saveLastReadMessage(@Param("chatId") Long chatId, @Param("userId") String userId, @Param("messageId") Long messageId);

    @Query(nativeQuery = true, value = "select exists(select * from forum.unread_messages where chat_id = :chatId and user_id = :userId);")
    boolean lastReadMessageIsExist(@Param("chatId") Long chatId, @Param("userId") String userId);

}
