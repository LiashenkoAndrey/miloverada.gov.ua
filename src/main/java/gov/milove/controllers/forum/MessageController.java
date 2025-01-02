package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.*;
import gov.milove.domain.forum.message.Message;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.MessageRepo;
import gov.milove.services.forum.MessageService;
import jakarta.persistence.EntityExistsException;
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

import static java.lang.String.format;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageController {


    private final MessageRepo messageRepo;
    private final ForumUserRepo forumUserRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @PostMapping("/forum/message/new")
    public Message newMessage(@Valid @RequestBody MessageDto dto) {
        Message message = MessageDto.toEntity(dto);
        message.setSender(forumUserRepo.getReferenceById(dto.getSenderId()));
        message.setChatId(dto.getChatId());

        Message saved = messageRepo.save(message);
        log.info(saved);
        return saved;
    }

    @PutMapping("/protected/forum/message/update")
    public Long update(@RequestBody @Valid UpdateMessageDto updated) {
        log.info("update message: " + updated);
        Message message = messageRepo.findById(updated.getId()).orElseThrow(EntityExistsException::new);
        message.setText(updated.getText());
        Message saved = messageRepo.save(message);
        messagingTemplate.convertAndSend("/chat/" + updated.getChatId()  + "/updateMessageEvent", message);
        return saved.getId();
    }

    @Transactional
    @MessageMapping("/userMessage/new")
    public void saveMessage(@Valid @Payload MessageDto dto) {
        messageService.saveNewMessage(dto);
    }


    @Transactional
    @MessageMapping("/messages/forward")
    public void forwardMessages(@Valid @Payload ForwardMessagesDto dto) {
        log.info("forward messages: {}" ,dto);
        messageService.forwardMessages(dto);
    }


    @MessageMapping("/userMessage/wasDeleted")
    public void notifyThatMessageWasDeleted(@Payload DeleteMessageDto dto) {
        log.info("delete message: " + dto);
        messagingTemplate.convertAndSend("/chat/" + dto.getChatId()  + "/deleteMessageEvent", dto);
    }

    @MessageMapping("/userMessage/image/wasDeleted")
    public void notifyThatMessageImageWasDeleted(@Payload DeleteMessageImageDto dto) {
        log.info("delete message image: " + dto);
        messagingTemplate.convertAndSend("/chat/" + dto.getChatId()  + "/deleteMessageImageEvent", dto);
    }

    @MessageMapping("/userMessage/lastReadId/update")
    public void setLastReadMessage(@Valid @Payload LastReadMessageDto dto) {
        log.info("setLastReadMessage: " + dto);
        if (messageRepo.lastReadMessageIsExist(dto.getChatId(), dto.getUserId())) {
            log.info("LastReadMessage exist, update...");
            messageRepo.updateLastReadMessage(dto.getChatId(), dto.getUserId(), dto.getMessageId());
        } else {
            log.info("LastReadMessage not exist, save...");
            messageRepo.saveLastReadMessage(dto.getChatId(), dto.getUserId(), dto.getMessageId());
        }
    }

    @DeleteMapping("/protected/forum/message/{id}/delete")
    public Long deleteMessageById(@PathVariable Long id) {
        return messageService.deleteById(id);
    }

    @GetMapping("/forum/message/all")
    public List<Message> getAllMessagesByChatId(@ModelAttribute MessageRequestDto dto) {
        log.info("message request: "+ dto);
        return messageService.getMessages(dto);
    }

    @GetMapping("/forum/message/newPage")
    public List<Message> getNewPageOfMessages(@ModelAttribute MessageRequestDto dto) {
        log.info("message request: "+ dto);
        return messageRepo.getNewPageOfMessages(dto.getChatId(), dto.getLastReadMessageId(), PageRequest.of(dto.getPageIndex(), dto.getPageSize()));
    }

    @GetMapping("/forum/chat/{chatId}/message/latest")
    public List<Message> getLatestOfChat(@PathVariable Long chatId) {
        List<Message> messages =  messageRepo.findAllByChatId(chatId, PageRequest.of(0, 30, Sort.by("createdOn").descending()));
        Collections.reverse(messages);
        return messages;
    }

    @GetMapping("/forum/message/latest")
    public List<Message> getLatestMessages() {
        return messageRepo.findAll(PageRequest.of(0, 30, Sort.by("createdOn").descending())).stream().collect(Collectors.toList());
    }

    @GetMapping("/forum/chat/{chatId}/messages/previous")
    public List<Message> getPrevious(@RequestParam Long fromMessageId, @PathVariable Long chatId) {
        List<Message> messages = messageRepo.getPrevious(fromMessageId, chatId);
        Collections.reverse(messages);
        return messages;
    }

    @GetMapping("/forum/chat/{chatId}/messages/next")
    public List<Message> getNext(@RequestParam Long fromMessageId, @PathVariable Long chatId) {
        return messageRepo.getNext(fromMessageId, chatId);
    }
}
