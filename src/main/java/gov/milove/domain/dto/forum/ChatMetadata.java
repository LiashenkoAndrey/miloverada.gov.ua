package gov.milove.domain.dto.forum;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMetadata {

    private Long last_read_message_id;

    private Long unread_messages_count;

    private Boolean is_member;
}
