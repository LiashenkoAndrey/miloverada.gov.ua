package gov.milove.domain.dto.admin;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NewNotificationDto {

    private String message;

    private String text;

    private String authorId;
}
