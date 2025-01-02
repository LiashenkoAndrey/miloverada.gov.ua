package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.*;
import gov.milove.domain.forum.chat.Chat;
import gov.milove.domain.forum.chat.PrivateChat;
import gov.milove.domain.forum.chat.UserChat;
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
     * Adds new chat to visited users chats list
     * or updates last visited time of existing chat by user
     *
     * @param forumUserId id of forum user
     * @param chatId      chat id
     */
    @Override
    public void addChatToVisitedUsersChatsOrUpdateIfExists(String forumUserId, Long chatId) {
        if (forumUserId == null) {
            log.error("Forum user id is null");
            return;
        };

        UserChat newRecord = new UserChat(forumUserId, chatRepo.getReferenceById(chatId));
        Optional<UserChat> chatVisitOpt = userChatRepo.findOne(Example.of(newRecord));
        if (chatVisitOpt.isEmpty()) {
            log.info("create new chat record...");
            userChatRepo.save(newRecord);
        } else {
            log.info("update user chat record...");
            UserChat userChat = chatVisitOpt.get();
            userChat.setLastVisitedOn(new Date());
            userChatRepo.save(userChat);
        }
    }

    /**
     * Calls when user want open or start a private chat with other user
     *
     * @param receiverId user which started chat
     * @param senderId   user that wants start chat
     * @return {@link PrivateChatDto}
     */
    @Override
    public PrivateChatDto findPrivateChatBetweenToUsers(String receiverId, String senderId) {
        Optional<PrivateChat> privateChatOptional = privateChatRepo.findPrivateChatBetweenToUsers(receiverId, senderId);
        log.info("get or create private chat, receiver = {}, senderId = {}", receiverId, senderId);

        if (privateChatOptional.isPresent()) {
            log.info("private chat already exists");
            return privateChatToDto(privateChatOptional.get(), receiverId);
        } else {
            log.info("private chat not exist, create new...");
            Chat newChat = chatRepo.save(new Chat(true));
            PrivateChat newPrivateChat = privateChatRepo.save(new PrivateChat(
                    forumUserRepo.getReferenceById(receiverId),
                    forumUserRepo.getReferenceById(senderId),
                    newChat.getId()
            ));
            log.info("new private chat id = {}, PrivateChat id = {} ", newChat.getId(), newPrivateChat.getId());

            return new PrivateChatDto(
                    newPrivateChat.getId(),
                    newPrivateChat.getChat_id(),
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
     * @param privateChat private chat entity
     * @param receiverIdDecoded receiver forum user id
     * @return PrivateChatDto
     */
    private static PrivateChatDto privateChatToDto(PrivateChat privateChat, String receiverIdDecoded) {

        PrivateChatDto privateChatDto = new PrivateChatDto(privateChat.getId(), privateChat.getChat_id());

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
    public ChatDto newTopicChat(NewChatDto dto) {
        Chat chat = NewChatDto.toDomain(dto);
        chat.setOwner(forumUserRepo.getReferenceById(dto.getOwnerId()));
        log.info("new chat: " + chat);
        Chat saved = chatRepo.save(chat);
        em.createNativeQuery("insert into topic_chats(topic_id, chats_id) VALUES (:topicId, :chatId)")
                .setParameter("topicId", dto.getTopicId())
                .setParameter("chatId", saved.getId())
                .executeUpdate();
        return chatRepo.findChatById(saved.getId());
    }



    @Override
    public List<ChatDtoWithMetadata> getUserChatsWithMetaById(String userId) {
//        List<Object[]> tuples = em.createQuery("""
//                     select distinct c, function('forum.get_chat_metadata', CAST(c.id as integer) , str(:userId) ) from Chat c
//                            join UserChat uc on c.id = uc.chat.id
//                            join PrivateChat pc on c.id = pc.chat_id
//                    where uc.userId = :userId
//
//                """, Object[].class)
//                .setParameter("userId", userId)
//                .getResultList();


        List<Object[]> tuples = em.createQuery("""
                        select c, function('forum.get_chat_metadata', CAST(c.id as integer) , str(:userId) ),
                          case
                              when c.isPrivate then (select concat(fu.nickname, '~', fu.id)
                                                      from ForumUser fu
                                                      where id = CASE
                                                                     WHEN pc.user1.id = :userId THEN pc.user2.id
                                                                     else pc.user1.id
                                                          END)
                          end
          from Chat c
                   join UserChat uc on c.id = uc.chat.id
                   join PrivateChat pc on (pc.user2.id = :userId or
                                                  pc.user1.id = :userId)
          
          where uc.userId = :userId order by uc.lastVisitedOn desc
                """, Object[].class)
                .setParameter("userId", userId)
                .getResultList();

        Map<String, ChatDtoWithMetadata> hashMap = new HashMap<>();


        tuples.stream()
                .map((tuple) -> {
                    log.info("get user chat metadata, userId = {}, tuple = {}", userId, tuple);
                    assert tuple[0] instanceof Chat;
                    Chat chat = (Chat) tuple[0];
                    ChatMetadata chatMetadata = parseMeta(tuple[1].toString());
                    String privateChatName = tuple[2] == null ? null : tuple[2].toString();
                    return new ChatDtoWithMetadata(chat, chatMetadata, parsePrivateChatMeta(privateChatName));
                })
                .forEach(((chatDtoWithMetadata) -> {
                    if (chatDtoWithMetadata.getPrivateChatMetadata() != null) {
                        if (!chatDtoWithMetadata.getPrivateChatMetadata().getUserId().equals(userId)) {
                            hashMap.put(chatDtoWithMetadata.getChat().getIdAlias(), chatDtoWithMetadata);
                            log.info("ok -" + chatDtoWithMetadata);
                        } else {
                            log.info("Found useless private chat= {}", chatDtoWithMetadata);
                        }
                    } else  {
                        log.info("No private chat found {}", chatDtoWithMetadata);
                        hashMap.put(chatDtoWithMetadata.getChat().getIdAlias(), chatDtoWithMetadata);

                    }
                }));

        log.info(hashMap.keySet());

            log.info(hashMap.values().stream().toList());
        return  hashMap.values().stream().toList();
    }

    private PrivateChatMetadata parsePrivateChatMeta(String s) {
        if (s == null) return null;
        String[] arr = s.split("~");
        String nickname = arr[0];
        String userId = arr[1];
        return new PrivateChatMetadata(nickname, userId);
    }

    private ChatMetadata parseMeta(String metaStr) {
        String[] arr = metaStr.replaceAll("\\(|\\)", "").split(",");
        Long last_read_message_id = arr[0].isEmpty() ? null : Long.parseLong(arr[0]);
        Long unread_messages_count = Long.parseLong(arr[1]);
        Boolean is_member = arr[2].equals("t");
        return new ChatMetadata(last_read_message_id, unread_messages_count, is_member);
    }
}
