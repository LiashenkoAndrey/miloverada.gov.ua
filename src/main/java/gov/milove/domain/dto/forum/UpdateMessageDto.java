package gov.milove.domain.dto.forum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateMessageDto {

    @NotNull
    private Long id;

    @NotNull
    private String text;

    @NotNull
    private Long chatId;
}
