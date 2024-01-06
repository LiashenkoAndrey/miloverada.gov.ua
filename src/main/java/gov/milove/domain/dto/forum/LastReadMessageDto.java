package gov.milove.domain.dto.forum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LastReadMessageDto {
    @NotNull
    private String userId;

    @NotNull
    private Long chatId;

    @NotNull
    private Long messageId;
}
