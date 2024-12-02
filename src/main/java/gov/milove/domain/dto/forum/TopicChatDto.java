package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.ForumUserDto;

public interface TopicChatDto {

    Long getId();

    Long getTopicId();

    String getName();

    String getDescription();

    String getPicture();

    String getCreatedOn();

    ForumUserDto getCreator();

    Long getTotalMessagesAmount();

    Long getTotalMembersAmount();
}
