package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.ForumUser;
import gov.milove.domain.forum.ForumUserDto;

public interface ChatDto {

    Long getId();

    String getName();

    String getDescription();

    String getPicture();

    String getCreatedOn();

    String getIsPrivate();

    ForumUserDto getOwner();

    Long getTotalMessagesAmount();

    Long getTotalMembersAmount();
}
