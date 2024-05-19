package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.ChatDto;
import gov.milove.domain.dto.forum.ChatDtoWithMetadata;
import gov.milove.domain.dto.forum.ChatMetadata;
import gov.milove.domain.dto.forum.NewChatDto;
import gov.milove.domain.forum.Chat;
import gov.milove.repositories.jpa.forum.ChatRepo;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.services.forum.ChatService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatServiceImpl implements ChatService {

    private final ForumUserRepo forumUserRepo;
    private final ChatRepo chatRepo;

    @PersistenceContext
    private EntityManager em;

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
        List<Object[]> tuples = em.createQuery("""
                     select c, function('forum.get_chat_metadata', CAST(c.id as integer) , str('google-oauth2|106434411070973160536') ) from Chat c 
                            join UserChat uc on c.id = uc.chat.id where uc.userId = 'google-oauth2|106434411070973160536'
                """, Object[].class)
                .getResultList();

        List<ChatDtoWithMetadata> res = tuples.stream()
                .map((tuple) -> {
                    assert tuple[0] instanceof Chat;
                    Chat chat = (Chat) tuple[0];
                    ChatMetadata chatMetadata = parseMeta(tuple[1].toString());
                    return new ChatDtoWithMetadata(chat, chatMetadata);
                })
                .toList();
        log.info(res);
        return res;
    }

    private ChatMetadata parseMeta(String metaStr) {
        String[] arr = metaStr.replaceAll("\\(|\\)", "").split(",");
        Long last_read_message_id = arr[0].isEmpty() ? null : Long.parseLong(arr[0]);
        Long unread_messages_count = Long.parseLong(arr[1]);
        Boolean is_member = arr[2].equals("t");
        return new ChatMetadata(last_read_message_id, unread_messages_count, is_member);
    }
}
