package gov.milove.domain.dto.forum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.milove.domain.forum.ForumUser;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PrivateChatDto {

    public PrivateChatDto(Long id, Long chat_id) {
        this.id = id;
        this.chat_id = chat_id;
    }

    public PrivateChatDto(Long id, Long chat_id, ForumUser sender, ForumUser receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.chat_id = chat_id;
    }

    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumUser sender;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumUser receiver;

    private Long chat_id;
}
