package gov.milove.domain.mongo;

import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "milove_images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MongoNewsImage {

    public MongoNewsImage(int hashCode) {
        this.hashCode = hashCode;
    }

    @Id
    private String id;

    @Field(name = "last_queried")
    private LocalDate lastQueried;

    @Field(name = "binary_image")
    private Binary binaryImage;

    private String fileName;

    private String contentType;

    private int hashCode;

    @Override
    public String toString() {
        return "MongoNewsImage{" +
                "id='" + id + '\'' +
                ", lastQueried=" + lastQueried +
                ", binaryImage=" + binaryImage.length() +
                ", fileName='" + fileName + '\'' +
                ", contentType='" + contentType + '\'' +
                ", hashCode=" + hashCode +
                '}';
    }
}
