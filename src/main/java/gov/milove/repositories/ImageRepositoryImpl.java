package gov.milove.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import jakarta.persistence.EntityExistsException;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class ImageRepositoryImpl implements ImageRepository {

    private final MongoDatabase mongoDatabase;

    public ImageRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    /**
     * This method saves image to mongoDB and returns id of image in database
     * @return {code:String} id image
     */
    @Override
    public String saveImage(MultipartFile file) {

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("milove");
        Document document = new Document();
        String id;
        try {
            document.append("filename", file.getName());
            document.append("binary_image", file.getBytes());
            mongoCollection.insertOne(document);
            id = mongoCollection.find(document).iterator().tryNext().get("_id").toString();
        } catch (IOException ex) {
            System.out.println("Error saving image!");
            return "error";
        }


        return id;
    }

    @Override
    public byte[] getImageById(String id) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("milove");
        FindIterable<Document> findIterable = mongoCollection.find(new Document("_id", new ObjectId(id)));
        Document document = Optional.ofNullable(findIterable.first()).orElseThrow(EntityExistsException::new);
        Binary binary = (Binary) document.get("binary_image");
        return binary.getData();
    }
}
