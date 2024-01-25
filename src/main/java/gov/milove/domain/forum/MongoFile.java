package gov.milove.domain.forum;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.MediaType;

@Document(collection = "forum_message_file")
@Getter
@Setter
@NoArgsConstructor
public class MongoFile {

    public MongoFile(byte[] file, String contentType) {
        this.file = new Binary(file);
        this.contentType = contentType;
    }

    @Id
    private String id;

    private Binary file;

    private String contentType;


    @Override
    public String toString() {
        return "MongoFile{" +
                "id='" + id + '\'' +
                ", file length=" + file.length() +
                '}';
    }
}
