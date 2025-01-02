package gov.milove.domain.media;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;

@Document(collection = "image")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class Image {

    public Image(MultipartFile image) {
        try {
            byte[] bytes = image.getBytes();
            this.binaryImage = new Binary(bytes);
            this.hashCode = Arrays.hashCode(bytes);
            this.contentType = image.getContentType();
            this.filename = image.getOriginalFilename();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Id
    private String id;

    @NotNull
    private Binary binaryImage;

    @NotNull
    private String filename;

    @NotNull
    private Integer hashCode;

    @NotNull
    private String contentType;
}
