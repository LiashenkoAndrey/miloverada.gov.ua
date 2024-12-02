package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.*;
import gov.milove.domain.forum.PrivateChat;
import gov.milove.domain.forum.TopicChat;
import gov.milove.domain.forum.UserChat;
import gov.milove.repositories.jpa.forum.ChatRepo;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.PrivateChatRepo;
import gov.milove.repositories.jpa.forum.UserChatRepo;
import gov.milove.services.forum.ChatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private final ForumUserRepo forumUserRepo;
    private final ChatRepo chatRepo;
    private final UserChatRepo userChatRepo;
    private final PrivateChatRepo privateChatRepo;

    @PersistenceContext
    private EntityManager em;


    /**
     * Adds new topicChat to visited users chats list
     * or updates last visited time of existing topicChat by user
     *
     * @param forumUserId id of forum user
     * @param chatId      topicChat id
     */
    @Override
    public void addChatToVisitedUsersChatsOrUpdateIfExists(String forumUserId, Long chatId) {
        if (forumUserId == null) {
            log.error("Forum user id is null");
            return;
        };

        UserChat newRecord = new UserChat(forumUserId, chatRepo.getReferenceById(chatId));
        Optional<UserChat> chatVisitOpt = userChatRepo.findByUserIdAndChatId(forumUserId, chatId);
        if (chatVisitOpt.isEmpty()) {
            log.info("create new topicChat record...");
            userChatRepo.save(newRecord);
        } else {
            log.info("update user topicChat record...");
            UserChat userChat = chatVisitOpt.get();
            userChat.setLastVisitedOn(new Date());
            userChatRepo.save(userChat);
        }
    }

    /**
     * Calls when user want open or start a private topicChat with other user
     *
     * @param receiverId user which started topicChat
     * @param senderId   user that wants start topicChat
     * @return {@link PrivateChatDto}
     */
    @Override
    public PrivateChatDto findPrivateChatBetweenToUsers(String receiverId, String senderId) {
        Optional<PrivateChat> privateChatOptional = privateChatRepo.findPrivateChatBetweenToUsers(receiverId, senderId);
        log.info("get or create private topicChat, receiver = {}, senderId = {}", receiverId, senderId);

        if (privateChatOptional.isPresent()) {
            log.info("private chat already exists");
            return privateChatToDto(privateChatOptional.get(), receiverId);
        } else {
            log.info("private chat not exist, create new...");
            PrivateChat newPrivateChat = privateChatRepo.save(new PrivateChat(
                    forumUserRepo.getReferenceById(receiverId),
                    forumUserRepo.getReferenceById(senderId)
            ));
            log.info("new private PrivateChat id = {} ", newPrivateChat.getId());

            return new PrivateChatDto(
                    newPrivateChat.getId(),
                    newPrivateChat.getUser1(),
                    newPrivateChat.getUser2()
            );
        }
    }

    /**
     * Converts PrivateChat entity to dto depending on requested forum user
     * PrivateChat table saves info about two users with columns: user1, user2
     * This method defines which column is a receiver user and creates an object PrivateChatDto depending on it
     *
     * @param privateChat private topicChat entity
     * @param receiverIdDecoded receiver forum user id
     * @return PrivateChatDto
     */
    private static PrivateChatDto privateChatToDto(PrivateChat privateChat, String receiverIdDecoded) {

        PrivateChatDto privateChatDto = new PrivateChatDto(privateChat.getId());

        // if receiver is user1 in privateChat
        if (privateChat.getUser1().getId().equals(receiverIdDecoded)) {
            privateChatDto.setReceiver(privateChat.getUser1());
            privateChatDto.setSender(privateChat.getUser2());
        } else {
            privateChatDto.setReceiver(privateChat.getUser2());
            privateChatDto.setSender(privateChat.getUser1());
        }
        return privateChatDto;
    }


    @Override
    @Transactional
    public TopicChatDto newTopicChat(NewTopicChatDto dto) {
        TopicChat topicChat = NewTopicChatDto.toDomain(dto);
        topicChat.setCreator(forumUserRepo.getReferenceById(dto.getOwnerId()));
        log.info("new topicChat: " + topicChat);
        TopicChat saved = chatRepo.save(topicChat);
        em.createNativeQuery("insert into topic_chats(topic_id, chats_id) VALUES (:topicId, :chatId)")
                .setParameter("topicId", dto.getTopicId())
                .setParameter("chatId", saved.getId())
                .executeUpdate();
        return chatRepo.findChatById(saved.getId());
    }

}
