package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.*;
import gov.milove.repositories.jpa.forum.ChatRepo;
import gov.milove.services.forum.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatRepo chatRepo;
    private final ChatService chatService;

    @GetMapping("/forum/chat/all")
    public List<ChatDto> getAllByTopicId(@RequestParam(required = false) Long topicId) {
        return chatRepo.findByTopicId(topicId);
    }

    @GetMapping("/protected/forum/user/{encodedUserId}/chats")
    public List<ChatDtoWithMetadata> getUserVisitedChats(@PathVariable String encodedUserId) {
        return chatService.getUserChatsWithMetaById(decodeUriComponent(encodedUserId));
    }

    /**
     * Gets chat by id
     * @param chatId chat id
     * @param encodedUserId encoded forum user id
     * @return {@link gov.milove.domain.dto.forum.ChatDto} DTO of chat entity
     */
    @GetMapping("/forum/chat/id/{chatId}")
    public ChatDto getChatById(@PathVariable Long chatId,
                               @RequestParam(required = false) String encodedUserId) {
        log.info("getChatById userId = {}", encodedUserId);
        chatService.addChatToVisitedUsersChatsOrUpdateIfExists(decodeUriComponent(encodedUserId), chatId);
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
    public PrivateChatDto findOrCreatePrivateBetweenTwoForumUser(@PathVariable String receiverId,
                                                                 @RequestParam String senderId) {

        String receiverIdDecoded = decodeUriComponent(receiverId);
        String senderIdDecoded = decodeUriComponent(senderId);
        log.info("findOrCreatePrivateBetweenTwoForumUser senderId = {}, receiverId= {}", senderIdDecoded, receiverIdDecoded);

        PrivateChatDto privateChatDto = chatService.findPrivateChatBetweenToUsers(receiverIdDecoded, senderIdDecoded);

        // add a new private chat to user visited chat list or update last visited time by user
        chatService.addChatToVisitedUsersChatsOrUpdateIfExists(senderIdDecoded, privateChatDto.getId());

        return privateChatDto;
    }

    @GetMapping("/forum/chat/id/{chatId}/metadata")
    private ChatMetadataDto getChatMetadata(@PathVariable Long chatId, @RequestParam String userId) {
        return chatRepo.getChatMetadata(chatId, decodeUriComponent(userId));
    }


    @PostMapping("/protected/forum/chat/new")
    public ChatDto newChat(@Valid @RequestBody NewChatDto dto) {
        return chatService.newTopicChat(dto);
    }


}
