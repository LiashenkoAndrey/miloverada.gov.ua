package gov.milove.repositories;

import com.mongodb.BasicDBObject;
import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import jakarta.persistence.EntityExistsException;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DocumentRepositoryMongo {
    private final MongoDatabase mongoDatabase;

    public DocumentRepositoryMongo(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }


    public String saveToMongo(MultipartFile file, String title) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        Document document = new Document();
        document.append("title", title);
        try {
            document.append("file", file.getBytes());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mongoCollection.insertOne(document);
        System.out.println("saved document " + file.getName());
        String id = mongoCollection.find(document).iterator().tryNext().get("_id").toString();
        return id;
    }

    public MultipartFile getFromMongoById(String id) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        FindIterable<Document> findIterable = mongoCollection.find(new Document("_id", new ObjectId(id)));
        Document document = Optional.ofNullable(findIterable.first()).orElseThrow(EntityExistsException::new);
        return (MultipartFile) document.get("file");
    }

    public void deleteDocumentsById(List<String> documentsIdList) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        for (String id : documentsIdList) {
            mongoCollection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        }

        System.out.println("All documents was deleted!");
    }

}
