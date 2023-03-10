package gov.milove.repositories.implementation;


import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import jakarta.persistence.EntityExistsException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Component
public class DocumentRepositoryMongo {
    private final MongoDatabase mongoDatabase;

    public DocumentRepositoryMongo(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }


    public void saveToMongo(MultipartFile file) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        Document document = new Document();
        document.append("name", file.getName());
        document.append("filename", file.getOriginalFilename());
        try {
            document.append("file", file.getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mongoCollection.insertOne(document);
    }

    public byte[] getFromMongoById(String id) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");

        FindIterable<Document> findIterable = mongoCollection.find(new Document("filename", id));
        Document document = Optional.ofNullable(findIterable.first()).orElseThrow(EntityExistsException::new);

        Binary binary = (Binary) document.get("file");
        return binary.getData();
    }

    public void deleteDocumentsById(List<String> documentsIdList) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        for (String id : documentsIdList) {
            mongoCollection.deleteOne(new Document("filename", id));
        }
        System.out.println("All documents was deleted!");
    }

    /**
     * Updates file by filename
     * @param document_id old filename
     * @param file new file
     */
    public void updateDocument(String document_id, MultipartFile file) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        byte[] binary_file;
        try {
            binary_file = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bson filter = eq("filename", document_id);
        Bson updateOperation = combine(set("file", binary_file), set("filename", file.getOriginalFilename()));
        UpdateResult updateResult = mongoCollection.updateOne(filter, updateOperation);
        System.out.println(updateResult);
    }
}
