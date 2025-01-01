package gov.milove.domain.forum.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "forum_message_image")
@Getter
@Setter
@NoArgsConstructor
public class MongoMessageImage {

    public MongoMessageImage(String image, Integer hashCode) {
        this.base64Image = image;
        this.hashCode = hashCode;
    }

    public MongoMessageImage(Integer hashCode) {
        this.hashCode = hashCode;
    }

    @Id
    private String id;

    private String base64Image;

    private Integer hashCode;

}
