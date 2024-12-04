package gov.milove.services.forum;

import gov.milove.domain.dto.forum.NewForumUserDto;
import gov.milove.domain.forum.ForumUser;

public interface ForumUserService {

    ForumUser saveNewUser(NewForumUserDto dto, String appUserId);


}
