package gov.milove.repositories.impl;

import com.mongodb.client.MongoCollection;
import gov.milove.exceptions.ImageNotFoundException;
import gov.milove.exceptions.ImageRepositoryException;
import gov.milove.repositories.ImageRepository;
import org.bson.Document;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

@Controller
public class ImageRepositoryImpl implements ImageRepository {

    private static final Logger logger = LoggerFactory.getLogger(ImageRepositoryImpl.class);

    private final MongoCollection<Document> collection;

    public ImageRepositoryImpl(@Qualifier("imageCollection") MongoCollection<Document> collection) {
        this.collection = collection;
    }


    /**
     * This method saves image to mongoDB and returns id of image in database
     * @return String id image
     */
    @Override
    public String saveImage(MultipartFile file) {
        String id;
        try {
            Document document = new Document()
                .append("filename", file.getName())
                .append("binary_image", file.getBytes())
                .append("last_queried", LocalDate.now());

            collection.insertOne(document);
            Document savedImage = collection.find(document).first();

            if (savedImage != null) {
                id = savedImage.get("_id").toString();
            } else throw new IOException("can't find document with filename:" + file.getName());

        } catch (IOException e) {
            logger.error(e.toString());
            throw new ImageRepositoryException(e);
        }

        return id;
    }

    /**
     * @param image_id image id
     * @param file file
     */
    @Override
    public void updateImage(String image_id, MultipartFile file) {
        try {
            collection.updateOne(
                    eq("_id", new ObjectId(image_id)),
                    combine(
                            set("binary_image", file.getBytes()),
                            set("filename", file.getName()),
                            set("last_queried", LocalDate.now())
                    )
            );

        } catch (IOException e) {
            logger.error(e.toString());
            throw new ImageRepositoryException(e);
        }
    }

    @Override
    public byte[] getImageById(String id) {
        Document document = collection.findOneAndUpdate(
                new Document("_id", new ObjectId(id)),
                set("last_queried", LocalDate.now())
        );

        if (document != null) {
            return ((Binary) document.get("binary_image")).getData();
        } else {
            throw new ImageNotFoundException("image with id: " + id +" not found!");
        }
    }

    public void deleteImageById(String image_id) {
        collection.deleteOne(new Document("_id", new ObjectId(image_id)));
    }

}
