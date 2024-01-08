package gov.milove.services.forum;

import gov.milove.domain.dto.forum.MessageDto;
import gov.milove.domain.dto.forum.MessageRequestDto;
import gov.milove.domain.forum.Message;

import java.util.List;

public interface MessageService {



    List<Message> getMessages(MessageRequestDto dto);

    Long deleteById(Long id);

    Message saveMessage(MessageDto messageDto);
}
