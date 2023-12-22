package gov.milove.controllers.forum;

import gov.milove.domain.MessageDto;
import gov.milove.domain.forum.Message;
import gov.milove.repositories.ChatRepo;
import gov.milove.repositories.ForumUserRepo;
import gov.milove.repositories.MessageRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forum/message")
public class MessageController {


    private final MessageRepo messageRepo;
    private final ForumUserRepo forumUserRepo;
    private final ChatRepo chatRepo;
    private final SimpMessagingTemplate messagingTemplate;


    @PostMapping("/new")
    public Message newMessage(@Valid @RequestBody MessageDto dto) {
        Message message = MessageDto.toEntity(dto);
        message.setSender(forumUserRepo.getReferenceById(dto.getSenderId()));
        message.setChat(chatRepo.getReferenceById(dto.getChatId()));

        Message saved = messageRepo.save(message);
        log.info(saved);
        return saved;
    }

    @MessageMapping("/userMessage/new")
    @Transactional
    public void broadcastMessage(@Valid @Payload MessageDto dto) {
        log.info("new message: " + dto);

        Message message = MessageDto.toEntity(dto);
        message.setSender(forumUserRepo.getReferenceById(dto.getSenderId()));
        message.setChat(chatRepo.getReferenceById(dto.getChatId()));
        Message saved = messageRepo.save(message);

        String destination = "/chat/" + dto.getChatId();
        messagingTemplate.convertAndSend(destination, saved);
    }



    @GetMapping("/all")
    public List<Message> getAllMessagesByChatId(@RequestParam Long chatId) {
        return  messageRepo.findAllByChat_Id(chatId);
    }

    @GetMapping("/latest")
    public List<Message> getLatest() {
        return messageRepo.findLatest(PageRequest.ofSize(20));
    }
}
