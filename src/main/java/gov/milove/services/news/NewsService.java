package gov.milove.services.news;

import gov.milove.domain.news.News;
import gov.milove.domain.dto.news.NewsDtoWithImageAndType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public interface NewsService {

    News save(News news, MultipartFile[] images, LocalDateTime dateOfPostponedPublication);

    void deleteById(Long id);

    void update(NewsDtoWithImageAndType news);

    void deleteNewsImageById(String mongoId);

}
