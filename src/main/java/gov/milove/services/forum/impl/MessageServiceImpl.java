package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.MessageDto;
import gov.milove.domain.dto.forum.MessageRequestDto;
import gov.milove.domain.forum.Message;
import gov.milove.domain.forum.MessageImage;
import gov.milove.repositories.forum.ChatRepo;
import gov.milove.repositories.forum.ForumUserRepo;
import gov.milove.repositories.forum.MessageRepo;
import gov.milove.repositories.mongo.MessageImageRepo;
import gov.milove.services.forum.MessageImageService;
import gov.milove.services.forum.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;


@Service
@Log4j2
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepo messageRepo;

    private final ForumUserRepo forumUserRepo;

    private final ChatRepo chatRepo;

    private final MessageImageService imageService;

    private final MessageImageService messageImageService;


    @Override
    public List<Message> getMessages(MessageRequestDto dto) {
        log.info("getMessages: " +dto);
        if (dto.getLastReadMessageId() != null) {
            log.info("last msg not null: " + dto.getLastReadMessageId());
            return messageRepo.findAllFromLastReadMessageInRange(
                    dto.getChatId(),
                    dto.getLastReadMessageId(),
                    (dto.getPageSize() * 2) -1,
                    PageRequest.of(dto.getPageIndex(), dto.getPageSize() * 2)
            );
        }
        log.info("last msg is null, load default page");
        Pageable pageReq = PageRequest.of(
                dto.getPageIndex(),
                dto.getPageSize(),
                Sort.by("createdOn")
                        .descending()
        );

        List<Message> messages = messageRepo.findAllByChat_Id(dto.getChatId(), pageReq);
        Collections.reverse(messages);
        return messages;
    }

    @Override
    @Transactional
    public Long deleteById(Long id) {
        Message message = messageRepo.findById(id).orElseThrow(EntityNotFoundException::new);

        messageImageService.deleteImagesIfNotUsedMoreThenOneTime(message.getImagesList());
        chatRepo.deleteFromLastReadMessagesTable(id);
        messageRepo.deleteById(id);
        return id;
    }



    @Override
    public Message saveMessage(MessageDto dto) {
        Message message = MessageDto.toEntity(dto);
        message.setSender(forumUserRepo.getReferenceById(dto.getSenderId()));
        message.setChat(chatRepo.getReferenceById(dto.getChatId()));

        if (dto.getReplyToMessageId() != null) {
            Message message1 = messageRepo.getReferenceById(dto.getReplyToMessageId());
            log.info("is reply! {}, reply message={}", dto.getReplyToMessageId(), message1);

            message.setRepliedMessage(message1);
        }

        if (!dto.getImagesDtoList().isEmpty()) {
            message.setImagesList(imageService.saveImages(dto.getImagesDtoList()));
        }
        return messageRepo.save(message);
    }
}
