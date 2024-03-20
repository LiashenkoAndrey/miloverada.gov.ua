package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.Chat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDtoWithMetadata  {

    private Chat chat;

    private ChatMetadata chatMetadata;

}
