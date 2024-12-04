package gov.milove.domain.dto.forum;

import java.util.List;

public interface TopicDto {

    Long getId();

    String getName();

    String getDescription();

    List<ChatDto> getChats();
}
