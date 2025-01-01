package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.message.Message;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class MessageDto {

    @Size(max = 3000)
    private String text;

    @NotNull
    private String senderId;

    @NotNull
    private Long chatId;

    private List<MessageImageDto> imagesDtoList = new ArrayList<>();

    private Long replyToMessageId;
    private Long forwardMessageId;

    List<FileDto> fileDtoList;

    public static Message toEntity(MessageDto dto) {
        return new Message(dto.getText());
    }


    @Override
    public String toString() {
        return "MessageDto{" +
                "text='" + text + '\'' +
                ", senderId='" + senderId + '\'' +
                ", chatId=" + chatId +
                ", imagesDtoList size=" + imagesDtoList.size() +
                '}';
    }
}
