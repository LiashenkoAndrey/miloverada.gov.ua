package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.ForumUserDto;

import java.util.Date;
import java.util.List;

public interface PostDto {

    Long getId();

    ForumUserDto getAuthor();

    String getText();

    String getImageId();

    Date getCreatedOn();

    Long getLikesAmount();

    Boolean getIsUserLikedPost();

    List<PostCommentDto> getComments();

    Long getCommentsTotalAmount();
}
