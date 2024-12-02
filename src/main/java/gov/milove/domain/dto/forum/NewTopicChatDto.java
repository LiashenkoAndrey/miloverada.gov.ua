package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.TopicChat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewTopicChatDto {

    @NotNull
    private String name;

    @NotNull
    private String description;

    private String picture;

    @NotNull
    private String ownerId;

    @NotNull
    private Long topicId;

    public static TopicChat toDomain(NewTopicChatDto dto) {
        return new TopicChat(dto.getName(), dto.getDescription(), dto.getPicture());
    }
}
