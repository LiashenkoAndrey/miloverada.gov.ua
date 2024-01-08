package gov.milove.domain.dto.forum;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class DeleteMessageDto {

    private Long messageId;

    private Long chatId;
}
