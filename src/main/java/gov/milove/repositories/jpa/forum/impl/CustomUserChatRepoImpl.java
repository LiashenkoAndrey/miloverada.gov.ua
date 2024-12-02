package gov.milove.repositories.jpa.forum.impl;

import gov.milove.domain.dto.forum.ChatMetadata;
import gov.milove.domain.forum.ForumUser;
import gov.milove.domain.forum.PrivateChat;
import gov.milove.domain.forum.UserChat;
import gov.milove.domain.forum.UserChatWithMeta;
import gov.milove.repositories.jpa.forum.CustomUserChatRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
@Repository
public class CustomUserChatRepoImpl implements CustomUserChatRepo {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserChatWithMeta> getUserChatsWithMetadata(String userId) {
        log.info("getUserChatsWithMetadata");
        List<Object[]> tuples = em.createQuery("select uc, function('forum.get_chat_metadata', CAST(uc.id as integer) , str(:userId) ) from UserChat uc where uc.userId = :userId order by uc.lastVisitedOn desc ",
                        Object[].class)
                .setParameter("userId", userId)
                .getResultList();

        log.info("tuples = {}", tuples);
        List<UserChatWithMeta> userChatsWithMeta = tuples.stream()
                .map((tuple) -> {
                    ChatMetadata chatMetadata = parseChatMeta(Objects.requireNonNull(((PGobject) tuple[1]).getValue()));
                    UserChat userChat = (UserChat) tuple[0];
                    log.info("tuple = {}, chatMetadata = {}, userChat = {}", tuple[1], chatMetadata, userChat);
                    if (userChat.getChat() instanceof PrivateChat) {
                        userChat.setChat(swapUsersIfNeed((PrivateChat) userChat.getChat(), userId));
                    }

                    return new UserChatWithMeta(userChat, chatMetadata);
                })
                .toList();

        log.info("userChatsWithMeta = {}", userChatsWithMeta);

        return userChatsWithMeta;
    }

    /**
     *
     * @param privateChat private topicChat entity
     * @param receiverIdDecoded receiver forum user id
     * @return PrivateChat
     */
    private static PrivateChat swapUsersIfNeed(PrivateChat privateChat, String receiverIdDecoded) {
        log.info("swapUsersIfNeed receiverIdDecoded = {}, id = {}, eq = {}", receiverIdDecoded, privateChat.getUser1().getId() , privateChat.getUser1().getId().equals(receiverIdDecoded));
        if (privateChat.getUser1().getId().equals(receiverIdDecoded)) {
            ForumUser temp = privateChat.getUser2();
            privateChat.setUser2(privateChat.getUser1());
            privateChat.setUser1(temp);
        }
        return privateChat;
    }

    private ChatMetadata parseChatMeta(String metaStr) {
        String[] arr = metaStr.replaceAll("\\(|\\)", "").split(",");
        Long last_read_message_id = arr[0].isEmpty() ? null : Long.parseLong(arr[0]);
        Long unread_messages_count = Long.parseLong(arr[1]);
        Boolean is_member = arr[2].equals("t");
        return new ChatMetadata(last_read_message_id, unread_messages_count, is_member);
    }
}
