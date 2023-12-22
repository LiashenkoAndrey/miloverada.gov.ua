package gov.milove.domain;

import gov.milove.domain.forum.Message;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MessageDto {

    @Size(max = 3000)
    @NotNull
    private String text;

    @NotNull
    private Long senderId;

    @NotNull
    private Long chatId;

    public static Message toEntity(MessageDto dto) {
        return new Message(dto.getText());
    }
}
