package gov.milove.domain.forum;

import gov.milove.domain.dto.forum.ChatMetadata;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper=true)
public class UserChatWithMeta extends UserChat {

    public UserChatWithMeta(UserChat userChat, ChatMetadata chatMetadata) {
        super(userChat.getId(), userChat.getUserId(), userChat.getChat(), userChat.getLastVisitedOn());
        this.metadata = chatMetadata;
    }

    private ChatMetadata metadata;
}
