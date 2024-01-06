package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.LastReadMessageDto;
import gov.milove.domain.dto.forum.MessageDto;
import gov.milove.domain.dto.forum.MessageRequestDto;
import gov.milove.domain.forum.Message;
import gov.milove.repositories.forum.ChatRepo;
import gov.milove.repositories.forum.ForumUserRepo;
import gov.milove.repositories.forum.MessageRepo;
import gov.milove.services.forum.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/forum")
public class MessageController {


    private final MessageRepo messageRepo;
    private final ForumUserRepo forumUserRepo;
    private final ChatRepo chatRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @PostMapping("/message/new")
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

    @MessageMapping("/userMessage/lastReadId/update")
    public void setLastReadMessage(@Valid @Payload LastReadMessageDto dto) {
        log.info("setLastReadMessage: " + dto);
        if (messageRepo.lastReadMessageIsExist(dto.getChatId(), dto.getUserId())) {
            log.info("LastReadMessage exist, update...");
//            messageRepo.updateLastReadMessage(dto.getChatId(), dto.getUserId(), dto.getMessageId());
        } else {
            log.info("LastReadMessage not exist, save...");
            messageRepo.saveLastReadMessage(dto.getChatId(), dto.getUserId(), dto.getMessageId());
        }
    }


    @GetMapping("/message/all")
    public List<Message> getAllMessagesByChatId(@ModelAttribute MessageRequestDto dto) {
        log.info("message request: "+ dto);
        return messageService.getMessages(dto);
    }

    @GetMapping("/message/newPage")
    public List<Message> getNewPageOfMessages(@ModelAttribute MessageRequestDto dto) {
        log.info("message request: "+ dto);
        return messageRepo.getNewPageOfMessages(dto.getChatId(), dto.getLastReadMessageId(), PageRequest.of(dto.getPageIndex(), dto.getPageSize()));
    }

    @GetMapping("/chat/{chatId}/message/latest")
    public List<Message> getLatestOfChat(@PathVariable Long chatId) {
        List<Message> messages =  messageRepo.findByChat_Id(chatId, PageRequest.of(0, 30, Sort.by("createdOn").descending()));
        Collections.reverse(messages);
        return messages;
    }

    @GetMapping("/message/latest")
    public List<Message> getLatestMessages() {
        return messageRepo.findAll(PageRequest.of(0, 30, Sort.by("createdOn").descending())).stream().collect(Collectors.toList());
    }
}
