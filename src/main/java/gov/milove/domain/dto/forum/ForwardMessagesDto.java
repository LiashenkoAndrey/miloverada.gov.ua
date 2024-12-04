package gov.milove.domain.dto.forum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Setter
public class ForwardMessagesDto {

    @NotEmpty(message = "In needs at least a one message to forward")
    private List<Long> messagesIdList = new ArrayList<>();

    @NotNull(message = "fromChatId is not present")
    private Long fromChatId;

    @NotEmpty(message = "In needs at least a one chat where forward messages")
    private List<Long> toChatsIdList = new ArrayList<>();

    private String encodedSenderId;

}
