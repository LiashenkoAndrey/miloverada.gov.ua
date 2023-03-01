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
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
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


    public void saveToMongo(MultipartFile file) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        Document document = new Document();
        System.out.println("file.getOriginalFilename() : " + file.getOriginalFilename());
        document.append("name", file.getName());
        document.append("filename", file.getOriginalFilename());
        document.append("content_type", file.getContentType());
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
        String filename = (String) document.get("filename");
        String name = (String) document.get("name");
        String contentType = (String) document.get("content_type");
        return binary.getData();
    }

    public void deleteDocumentsById(List<String> documentsIdList) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("document");
        for (String id : documentsIdList) {
            mongoCollection.deleteOne(Filters.eq("filename", id));
        }

        System.out.println("All documents was deleted!");
    }

}
