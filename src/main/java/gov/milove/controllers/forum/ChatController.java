package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.ChatMetadataDto;
import gov.milove.domain.dto.forum.NewChatDto;
import gov.milove.domain.forum.Chat;
import gov.milove.domain.forum.Message;
import gov.milove.domain.forum.PrivateChat;
import gov.milove.repositories.forum.ChatRepo;
import gov.milove.repositories.forum.ForumUserRepo;
import gov.milove.repositories.forum.PrivateChatRepo;
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

    @GetMapping("/forum/chat/all")
    public List<Chat> getAllByTopicId(@RequestParam(required = false) Long topicId) {
        return chatRepo.findByTopicId(topicId);
    }


    @GetMapping("/forum/chat/id/{chatId}")
    public Chat getChatById(@PathVariable Long chatId) {
        return chatRepo.findById(chatId).orElseThrow(EntityNotFoundException::new);
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
