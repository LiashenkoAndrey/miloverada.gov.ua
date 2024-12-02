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

    public PrivateChatDto(Long id) {
        this.id = id;
    }

    public PrivateChatDto(Long id, ForumUser sender, ForumUser receiver) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
    }

    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumUser sender;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumUser receiver;
}
