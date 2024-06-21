package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.*;
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

    /**
     * Frontend calls this endpoint when user opens a private chat with other forum user
     * If chat doesn't exist it will be created
     *
     * @param receiverId the user with whom the chat was opened
     * @param senderId   user witch sends messages
     * @return PrivateChatDto
     */
    @PostMapping("/protected/forum/user/{receiverId}/chat")
    public PrivateChatDto createPrivateChat(@PathVariable String receiverId,
                                            @RequestParam String senderId) {
        String receiverIdDecoded = decodeUriComponent(receiverId);
        String senderIdDecoded = decodeUriComponent(senderId);

        Optional<PrivateChat> privateChatOptional = privateChatRepo.findPrivateChatBetweenToUsers(receiverIdDecoded, senderIdDecoded);
        log.info("get or save chat, receiver = {}, senderId = {}", receiverIdDecoded, senderIdDecoded);

        if (privateChatOptional.isPresent()) {
            log.info("private chat already exists");
            return privateChatToDto(privateChatOptional.get(), receiverIdDecoded);
        } else {

            log.info("private chat not exist, create new...");
            Chat newChat = chatRepo.save(new Chat(true));
            PrivateChat newPrivateChat = privateChatRepo.save(new PrivateChat(
                    forumUserRepo.getReferenceById(receiverIdDecoded),
                    forumUserRepo.getReferenceById(senderIdDecoded),
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
     * PrivateChat table saved info about two user with columns user1 and user2
     * This method defines which column is receiver user and created an object PrivateChatDto depending on it
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
