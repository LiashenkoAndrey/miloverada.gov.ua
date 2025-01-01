package gov.milove.domain.mongo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "document")
@Getter
@Setter
@ToString
public class MongoDocument {

    public MongoDocument(String filename, Binary file, String contentType) {
        this.filename = filename;
        this.file = file;
        this.contentType = contentType;
    }

    @Id
    private String id;

    private String filename;

    private Binary file;

    private String contentType;


}
