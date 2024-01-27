package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.ChatMetadataDto;
import gov.milove.domain.dto.forum.NewChatDto;
import gov.milove.domain.forum.Chat;
import gov.milove.domain.forum.Message;
import gov.milove.repositories.forum.ChatRepo;
import gov.milove.repositories.forum.ForumUserRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatRepo chatRepo;
    private final ForumUserRepo forumUserRepo;

    @GetMapping("/forum/chat/all")
    public List<Chat> getAllByTopicId(@RequestParam(required = false) Long topicId) {
        return chatRepo.findByTopicId(topicId);
    }


    @GetMapping("/forum/chat/id/{chatId}")
    public Chat getChatById(@PathVariable Long chatId) {
        return chatRepo.findById(chatId).orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping("/protected/forum/privateChat/create")
    public ResponseEntity createPrivateChat(@RequestParam Long writerId,
                                            @RequestParam Long addresseeId) {
        return null;

    }


    @GetMapping("/forum/chat/id/{chatId}/metadata")
    private ChatMetadataDto getChatMetadata(@PathVariable Long chatId, @RequestParam String userId) {
        return chatRepo.getChatMetadata(chatId, decodeUriComponent(userId));
    }

    @PostMapping("/protected/forum/chat/new")
    public Long newChat(@Valid @RequestBody NewChatDto dto) {
        Chat chat = NewChatDto.toDomain(dto);
        chat.setOwner(forumUserRepo.getReferenceById(dto.getOwnerId()));
        log.info("new chat: " + chat);
        Chat saved = chatRepo.save(chat);
        return saved.getId();
    }


}
