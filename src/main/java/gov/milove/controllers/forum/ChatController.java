package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.ChatDto;
import gov.milove.domain.dto.forum.ChatDtoWithMetadata;
import gov.milove.domain.dto.forum.ChatMetadataDto;
import gov.milove.domain.dto.forum.NewChatDto;
import gov.milove.domain.forum.Chat;
import gov.milove.domain.forum.UserChat;
import gov.milove.domain.forum.PrivateChat;
import gov.milove.repositories.jpa.forum.ChatRepo;
import gov.milove.repositories.jpa.forum.UserChatRepo;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.PrivateChatRepo;
import gov.milove.services.forum.ChatService;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatRepo chatRepo;
    private final ForumUserRepo forumUserRepo;
    private final PrivateChatRepo privateChatRepo;
    private final UserChatRepo userChatRepo;
    private final ChatService chatService;

    @GetMapping("/forum/chat/all")
    public List<ChatDto> getAllByTopicId(@RequestParam(required = false) Long topicId) {
        return chatRepo.findByTopicId(topicId);
    }

    @GetMapping("/protected/forum/user/{encodedUserId}/chats")
    public List<ChatDtoWithMetadata> getUserVisitedChats(@PathVariable String encodedUserId) {
        return chatService.getUserChatsWithMetaById(decodeUriComponent(encodedUserId));
    }


    @GetMapping("/forum/chat/id/{chatId}")
    public ChatDto getChatById(@PathVariable Long chatId, @RequestParam(required = false) String encodedUserId) {
        log.info(encodedUserId);
        if (encodedUserId != null) {
            String decodedUserId = decodeUriComponent(encodedUserId);
            UserChat newRecord = new UserChat(decodedUserId, chatRepo.getReferenceById(chatId));
            Optional<UserChat> chatVisitOpt = userChatRepo.findOne(Example.of(newRecord));
            if (chatVisitOpt.isEmpty()) {
                log.info("create...");

                userChatRepo.save(newRecord);
            } else {
                log.info("update...");
                UserChat userChat = chatVisitOpt.get();
                userChat.setLastVisitedOn(new Date());
                userChatRepo.save(userChat);
            }
        }
        return chatRepo.findChatById(chatId);
    }

    @PostMapping("/protected/forum/user/{user1_id}/chat")
    public PrivateChat createPrivateChat(@PathVariable String user1_id,
                                            @RequestParam String user2_id) {
        String user1_id_decoded = decodeUriComponent(user1_id);
        String user2_id_decoded = decodeUriComponent(user2_id);

        Optional<PrivateChat> privateChatOptional = privateChatRepo.findByUser1AndUser2(user1_id_decoded, user2_id_decoded);
        log.info("get or save chat, user1 = {}, user2 = {}", user1_id_decoded, user2_id_decoded);

        if (privateChatOptional.isPresent()) {
            log.info("private chat already exists");
            return privateChatOptional.get();
        } else {

            log.info("private chat not exist, create new...");
            Chat newChat = chatRepo.save(new Chat(true));
            PrivateChat newPrivateChat = privateChatRepo.save(new PrivateChat(
                    forumUserRepo.getReferenceById(user1_id_decoded),
                    forumUserRepo.getReferenceById(user2_id_decoded),
                    newChat.getId()
            ));
            log.info("new private chat id = {}, PrivateChat id = {} ", newChat.getId(), newPrivateChat.getId());
            return newPrivateChat;
        }
    }

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/forum/chat/id/{chatId}/metadata")
    private ChatMetadataDto getChatMetadata(@PathVariable Long chatId, @RequestParam String userId) {
        return chatRepo.getChatMetadata(chatId, decodeUriComponent(userId));
    }

//    @GetMapping("/forum/chat/idV2/{chatId}/metadata")
//    private ChatDtoWithMetadata getChatMetadataV2(@PathVariable Long chatId, @RequestParam String userId) {
//        return chatService.getUserChatsWithMetaById(chatId, userId);
//    }

    @PostMapping("/protected/forum/chat/new")
    public ChatDto newChat(@Valid @RequestBody NewChatDto dto) {
        return chatService.newTopicChat(dto);
    }


}
