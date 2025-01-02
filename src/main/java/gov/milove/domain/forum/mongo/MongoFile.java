package gov.milove.domain.forum.mongo;

import lombok.*;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "forum_message_file")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MongoFile {

    public MongoFile(byte[] file, String contentType, Long size, String name) {
        this.file = new Binary(file);
        this.contentType = contentType;
        this.size = size;
        this.name = name;
    }

    @Id
    private String id;

    private Binary file;

    private String contentType;

    private Long size;

    private String name;



    @Override
    public String toString() {
        return "MongoFile{" +
                "id='" + id + '\'' +
                ", file length=" + file.length() +
                '}';
    }
}
