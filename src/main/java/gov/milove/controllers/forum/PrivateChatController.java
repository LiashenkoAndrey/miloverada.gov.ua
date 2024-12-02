package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.PrivateChatDto;
import gov.milove.services.forum.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class PrivateChatController {

    private final ChatService chatService;

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
}
