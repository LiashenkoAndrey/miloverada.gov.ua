package gov.milove.controllers.forum;

import gov.milove.domain.forum.Chat;
import gov.milove.repositories.ChatRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forum")
public class ChatController {

    private final ChatRepo chatRepo;

    @GetMapping("/chat/all")
    public List<Chat> getAllByTopicId(@RequestParam Long topicId) {
        return chatRepo.findByTopicId(topicId);
    }

    @GetMapping("/chat/id/{chatId}")
    public Chat getChatById(@PathVariable Long chatId) {
        return chatRepo.findById(chatId).orElseThrow(EntityNotFoundException::new);
    }

}
