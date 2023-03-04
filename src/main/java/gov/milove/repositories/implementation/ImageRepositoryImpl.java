package gov.milove.repositories.implementation;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import gov.milove.repositories.ImageRepository;
import jakarta.persistence.EntityExistsException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Controller
public class ImageRepositoryImpl implements ImageRepository {

    private final MongoDatabase mongoDatabase;

    public ImageRepositoryImpl(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    /**
     * This method saves image to mongoDB and returns id of image in database
     * @return String id image
     */
    @Override
    public String saveImage(MultipartFile file) {

        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("milove_images");
        Document document = new Document();
        String id;
        try {
            document.append("filename", file.getName());
            document.append("binary_image", file.getBytes());
            mongoCollection.insertOne(document);
            id = mongoCollection.find(document).iterator().tryNext().get("_id").toString();
            System.out.println("image saved: " + id);
        } catch (IOException ex) {
            System.out.println("Error saving image!");
            return "error";
        }


        return id;
    }

    /**
     * @param image_id
     * @param file
     * @return
     */
    @Override
    public void updateImage(String image_id, MultipartFile file) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("milove_images");
        byte[] binary;
        try {
            binary = file.getBytes();
        }catch (IOException ex) {
            System.out.println("error");
            throw new RuntimeException(ex);
        }

        Bson filter = eq("_id", new ObjectId(image_id));
        Bson updateOperation = combine(set("binary_image", binary), set("filename", file.getName()));
        UpdateResult updateResult = mongoCollection.updateOne(filter, updateOperation);
        System.out.println(updateResult);
    }

    @Override
    public byte[] getImageById(String id) {
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("milove_images");
        FindIterable<Document> findIterable = mongoCollection.find(new Document("_id", new ObjectId(id)));
        Document document = Optional.ofNullable(findIterable.first()).orElseThrow(EntityExistsException::new);
        Binary binary = (Binary) document.get("binary_image");
        return binary.getData();
    }

    private Document prepareDocument(MultipartFile file) {
        Document document = new Document();
        try {
            document.append("binary_image", file.getBytes());
            document.append("filename", file.getName());
        } catch (IOException e) {
            System.out.println("Fail to save image: " + file.getName());
            throw new RuntimeException(e);
        }
        return document;
    }
}
