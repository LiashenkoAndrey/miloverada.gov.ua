package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.chat.Chat;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ChatDtoWithMetadata  {

    private Chat chat;

    private ChatMetadata chatMetadata;

    private PrivateChatMetadata privateChatMetadata;

}
