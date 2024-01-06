package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.ForumUserDto;
import gov.milove.domain.dto.forum.NewUserDto;
import gov.milove.domain.forum.ForumUser;
import gov.milove.repositories.forum.ForumUserRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import static gov.milove.util.Util.decodeUriComponent;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ForumUserRepo forumUserRepo;

    @MessageMapping("/user/startTyping")
    public void handleUserStartTyping(@Valid @Payload ForumUserDto dto) {
        log.info("user start typing, " + dto);
        messagingTemplate.convertAndSend("/chat/"+ dto.getChatId() +"/typingUsers", dto);
    }

    @PostMapping("/protected/user/new")
    public String newUser(@Valid @RequestBody NewUserDto dto) {
        log.info("new user: " + dto);
        ForumUser user = NewUserDto.toDomain(dto);
        log.info("user: " + user);
        ForumUser saved = forumUserRepo.save(user);
        return saved.getId();
    }

    @GetMapping("/protected/user/isRegistered/id/{encodedUserId}")
    private boolean isRegistered(@PathVariable String encodedUserId) {
        String decodedUserId = decodeUriComponent(encodedUserId);
        log.info("user id: " + decodedUserId);
        return forumUserRepo.existsById(decodedUserId);
    }

    @MessageMapping("/user/stopTyping")
    public void handleUserStopTyping(@Payload ForumUserDto dto) {
        log.info("User has stopped typing, " + dto);
        messagingTemplate.convertAndSend("/chat/"+ dto.getChatId() +"/userStopTyping", dto);
    }
}
