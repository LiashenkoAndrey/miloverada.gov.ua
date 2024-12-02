package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.NewTopicChatDto;
import gov.milove.domain.dto.forum.TopicChatDto;
import gov.milove.domain.forum.TopicChat;
import gov.milove.repositories.jpa.forum.ChatRepo;
import gov.milove.services.forum.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class TopicChatController {

    private final ChatRepo chatRepo;
    private final ChatService chatService;

    /**
     * Creates a new topic chat
     * @param dto DTO of new topiv chat
     * @return DTO of {@link TopicChat} entity
     */
    @PostMapping("/protected/forum/chat/new")
    public TopicChatDto newTopicChat(@Valid @RequestBody NewTopicChatDto dto) {
        return chatService.newTopicChat(dto);
    }

    /**
     * Gets topic chat by id
     * @param chatId chat id
     * @param encodedUserId encoded forum user id
     * @return {@link TopicChatDto} DTO of topic chat entity
     */
    @GetMapping("/forum/chat/id/{chatId}")
    public TopicChatDto getTopicChatById(@PathVariable Long chatId,
                                         @RequestParam(required = false) String encodedUserId) {
        log.info("getTopicChatById userId = {}", decodeUriComponent(encodedUserId));
        chatService.addChatToVisitedUsersChatsOrUpdateIfExists(decodeUriComponent(encodedUserId), chatId);
        return chatRepo.findChatById(chatId);
    }
}
