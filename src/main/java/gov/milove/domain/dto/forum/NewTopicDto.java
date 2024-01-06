package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.Topic;
import lombok.Getter;

@Getter
public class NewTopicDto {
    private String name;
    private String description;

    public static Topic toDomain(NewTopicDto dto) {
        return new Topic(dto.getName(), dto.getDescription());
    }
}
