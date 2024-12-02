package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.UserChatWithMeta;

import java.util.List;

public interface CustomUserChatRepo {

    List<UserChatWithMeta> getUserChatsWithMetadata(String userId);
}
