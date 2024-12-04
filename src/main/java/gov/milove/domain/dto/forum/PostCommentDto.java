package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.ForumUserDto;

import java.util.Date;

public interface PostCommentDto {

    Long getId();

    String getText();

    ForumUserDto getAuthor();

    Date getCreatedOn();
}
