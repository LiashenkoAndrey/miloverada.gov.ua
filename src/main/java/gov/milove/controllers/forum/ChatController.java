package gov.milove.controllers.forum;

import static gov.milove.util.Util.decodeUriComponent;

import gov.milove.domain.dto.forum.ChatMetadataDto;
import gov.milove.domain.forum.UserChatWithMeta;
import gov.milove.repositories.jpa.forum.ChatRepo;
import gov.milove.repositories.jpa.forum.CustomUserChatRepo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatRepo chatRepo;
    private final CustomUserChatRepo customUserChatRepo;

    @GetMapping("/protected/forum/user/{encodedUserId}/chats")
    public List<UserChatWithMeta> getUserVisitedChats(@PathVariable String encodedUserId) {
        log.info("getUserVisitedChats");
        return customUserChatRepo.getUserChatsWithMetadata(decodeUriComponent(encodedUserId));
    }

    @GetMapping("/forum/chat/id/{chatId}/metadata")
    private ChatMetadataDto getChatMetadata(@PathVariable Long chatId, @RequestParam String userId) {
        return chatRepo.getChatMetadata(chatId, decodeUriComponent(userId));
    }
}
