package gov.milove.services.forum;

import gov.milove.domain.dto.forum.ForwardMessagesDto;
import gov.milove.domain.dto.forum.MessageDto;
import gov.milove.domain.dto.forum.MessageRequestDto;
import gov.milove.domain.forum.message.Message;

import java.util.List;

public interface MessageService {

    Message saveNewMessage(MessageDto messageDto);

    void forwardMessages(ForwardMessagesDto dto);

    List<Message> getMessages(MessageRequestDto dto);

    Long deleteById(Long id);

    Message saveMessage(MessageDto messageDto);
}
