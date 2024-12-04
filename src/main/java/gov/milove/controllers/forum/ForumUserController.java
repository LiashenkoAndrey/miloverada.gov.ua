package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.ForumUserDto;
import gov.milove.domain.dto.forum.UpdateUserOnlineStatusDto;
import gov.milove.domain.dto.forum.NewForumUserDto;
import gov.milove.domain.forum.ForumUser;
import gov.milove.exceptions.forum.ForumUserNotFoundException;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.services.forum.ForumUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ForumUserController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ForumUserRepo forumUserRepo;
    private final ForumUserService forumUserService;


    @MessageMapping("/user/startTyping")
    public void handleUserStartTyping(@Valid @Payload ForumUserDto dto) {
        log.info("user start typing, " + dto);
        messagingTemplate.convertAndSend("/chat/"+ dto.getChatId() +"/typingUsers", dto);
    }

    @GetMapping("/forum/users")
    private List<ForumUser> getAll() {
        return forumUserRepo.findAll();
    }

    @PostMapping("/protected/forum/user/new")
    public ForumUser newForumUser(@Valid @ModelAttribute NewForumUserDto dto, @RequestParam String encodedAppUserId) {
        log.info("new user, dto - {} ", dto);
        String decodedAppUserId = decodeUriComponent(encodedAppUserId);
        ForumUser user = forumUserService.saveNewUser(dto, decodedAppUserId);
        log.info("user - {}" , user);
        return user;
    }

    @GetMapping("/protected/forum/user/{encodedId}")
    public JSONObject getForumUserById(@PathVariable String encodedId) {
        String decodedId = decodeUriComponent(encodedId);
        JSONObject jsonObject = new JSONObject();
        if (forumUserRepo.existsById(decodedId)) {
            ForumUser user = forumUserRepo.findById(decodedId).orElseThrow(ForumUserNotFoundException::new);

            jsonObject.put("forumUser", user);
            jsonObject.put("isRegistered", true);

        } else {
            jsonObject.put("isRegistered", false);
        }
        return jsonObject;
    }

    @MessageMapping("/forumUser/isOnlineStatus")
    public void notifyThatUserIsOnline(@Valid @Payload UpdateUserOnlineStatusDto dto) {
        log.info("notify that user is online, " + dto);
        dto.setDate(new Date());
        messagingTemplate.convertAndSend("/chat/user/"  + dto.getUserIdThatNeedsNotification() +"/onlineStatus", dto);
        forumUserRepo.updateUserOnlineStatusById(dto.getUserIdThatOnlineStatusNeedsToBeUpdated(), dto.getIsOnline());
    }


    @GetMapping("/protected/forum/user/isRegistered/id/{encodedUserId}")
    private boolean isRegistered(@PathVariable String encodedUserId) {
        String decodedUserId = decodeUriComponent(encodedUserId);
        log.info("user id - {}" , decodedUserId);
        return forumUserRepo.existsById(decodedUserId);
    }

    @MessageMapping("/user/stopTyping")
    public void handleUserStopTyping(@Payload ForumUserDto dto) {
        log.info("User has stopped typing, " + dto);
        messagingTemplate.convertAndSend("/chat/"+ dto.getChatId() +"/userStopTyping", dto);
    }

    @GetMapping("/forum/activeUsers")
    public Integer getActiveUsersAmount() {
        return forumUserRepo.getActiveUsersAmount();
    }
}
