package gov.milove.services;

import gov.milove.domain.NewsImage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NewsImagesService {

    List<NewsImage> saveAll(List<MultipartFile> files);

    void deleteAllIfNotUsed(List<NewsImage> newsImages);

    void deleteFromMongoIfNotUsed(String mongoId);
}
