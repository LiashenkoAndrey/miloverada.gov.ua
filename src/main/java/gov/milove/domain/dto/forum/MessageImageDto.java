package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.MongoMessageImage;
import gov.milove.exceptions.ServiceException;
import lombok.*;
import org.bson.types.Binary;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MessageImageDto {

    private String base64Image;

    public static MongoMessageImage toEntity(MessageImageDto messageImageDto) {
        return new MongoMessageImage(messageImageDto.getBase64Image(), messageImageDto.getBase64Image().hashCode());
    }
}
