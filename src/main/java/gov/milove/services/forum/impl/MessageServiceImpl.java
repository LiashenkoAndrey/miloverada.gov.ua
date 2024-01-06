package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.MessageRequestDto;
import gov.milove.domain.forum.Message;
import gov.milove.repositories.forum.ChatRepo;
import gov.milove.repositories.forum.MessageRepo;
import gov.milove.services.forum.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;


@Service
@Log4j2
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepo messageRepo;

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
}
