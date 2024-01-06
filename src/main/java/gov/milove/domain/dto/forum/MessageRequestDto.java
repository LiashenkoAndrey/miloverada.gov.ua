package gov.milove.domain.dto.forum;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class MessageRequestDto {

    @NotNull
    Long chatId;

    Integer pageSize;

    @NotNull
    Integer pageIndex;

    Long lastReadMessageId;
}
