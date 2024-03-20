package gov.milove.services.forum;

import gov.milove.domain.dto.forum.ChatDto;
import gov.milove.domain.dto.forum.ChatDtoWithMetadata;
import gov.milove.domain.dto.forum.NewChatDto;

import java.util.List;

public interface ChatService {

    ChatDto newTopicChat(NewChatDto dto);

    List<ChatDtoWithMetadata> getUserChatsWithMetaById(String userId);
}
