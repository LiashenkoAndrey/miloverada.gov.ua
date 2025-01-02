package gov.milove.services.impl;

import gov.milove.domain.mongo.MongoNewsImage;
import gov.milove.domain.news.NewsImage;
import gov.milove.exceptions.ImageNotFoundException;
import gov.milove.exceptions.ServiceException;
import gov.milove.repositories.jpa.news.NewsImageRepo;
import gov.milove.repositories.mongo.NewsImagesMongoRepo;
import gov.milove.services.news.NewsImagesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bson.types.Binary;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class NewsImagesServiceImpl implements NewsImagesService {

    private final NewsImagesMongoRepo newsImagesMongoRepo;
    private final NewsImageRepo newsImageRepo;


    @Override
    public List<NewsImage> saveAll(List<MultipartFile> files) {
        List<MongoNewsImage> mongoImages = saveMongoFiles(files);
        log.info("images saved to mongoDb: {}", mongoImages);
        List<NewsImage> mapped = mongoImages.stream()
                .map((mongoNewsImage -> new NewsImage(mongoNewsImage.getFileName(), mongoNewsImage.getId())))
                .toList();
        log.info("images = {}", mapped);
        return newsImageRepo.saveAll(mapped);
    }

    @Override
    public void deleteAllIfNotUsed(List<NewsImage> newsImages) {
        for(NewsImage newsImage : newsImages) {
            deleteFromMongoIfNotUsed(newsImage.getMongoImageId());
        }
    }

    @Override
    public void deleteFromMongoIfNotUsed(String mongoId) {
        if (!newsImageRepo.newsImageIsUsedMoreThenOneTime(mongoId)) {
            log.info("Image = {}, is not used", mongoId );
            newsImagesMongoRepo.deleteById(mongoId);
        } else {
            log.info("Image = {}, is used", mongoId );
        }
    }


    private List<MongoNewsImage> saveMongoFiles(List<MultipartFile> files) {
        List<MongoNewsImage> mongoNewsImages = new ArrayList<>(files.size());

        for (MultipartFile file : files) {
            MongoNewsImage image = saveOrFindOfExisting(file);
            mongoNewsImages.add(image);
        }
        return mongoNewsImages;
    }

    private MongoNewsImage saveOrFindOfExisting(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            int hashCode = Arrays.hashCode(bytes);
            String contentType = file.getContentType();
            MongoNewsImage image;
            Example<MongoNewsImage> example = Example.of(new MongoNewsImage(hashCode));
            log.info("save image: contentType={}, hashCode={}", contentType, hashCode);
            log.info("save file = {}", file);
            log.info("Looking for an image...");
            if (newsImagesMongoRepo.exists(example)) {
                log.info("Image found");
                return newsImagesMongoRepo.findOne(example).orElseThrow(ImageNotFoundException::new);
            } else {
                log.info("Image not found");
                image = MongoNewsImage.builder()
                        .binaryImage(new Binary(bytes))
                        .fileName(file.getOriginalFilename())
                        .contentType(contentType)
                        .hashCode(hashCode)
                        .build();
                return newsImagesMongoRepo.save(image);
            }
        } catch (IOException e) {
            log.error(e);
            throw new ServiceException(e);
        }

    }
}
