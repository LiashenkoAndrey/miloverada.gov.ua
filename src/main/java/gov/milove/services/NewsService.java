package gov.milove.services;

import gov.milove.domain.News;
import gov.milove.domain.NewsType;
import gov.milove.domain.dto.NewsDTO;
import gov.milove.exceptions.NewsServiceException;
import gov.milove.repositories.NewsRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsService.class);

    private final NewsRepository newsRepository;
    private final ImageService imageService;


    /**
     * Find news by page number and newsAmount
     * @param page number of page
     * @param newsAmount amount of news in one page
     * @return page of news DTOs
     */

    public Page<NewsDTO> getPagesList(int page, int newsAmount) {
        return newsRepository.getPageOfDTO(PageRequest.of(page, newsAmount).withSort(Sort.Direction.DESC, "created"));
    }

    public void saveNews(String description, String main_text, MultipartFile image, String date, Long newsTypeId) {
        String savedImageId = imageService.saveImage(image);

        newsRepository.save(News.builder()
                .description(description)
                .main_text(main_text)
                .image_id(savedImageId)
                .news_type_id(newsTypeId)
                .created(date == null ? LocalDateTime.now() : LocalDateTime.of(LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalTime.now()))
                .build());
    }


    public boolean deleteNews(Long id) {
        boolean success;
        try {
            News news = newsRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            imageService.deleteImageById(news.getImage_id());
            newsRepository.delete(news);
            success = true;
        } catch (Exception ex) {
            success = false;
            logger.error(ex.toString());
            throw new NewsServiceException(ex);
        }
        return success;
    }

    public Optional<News> getNewsById(Long news_id) {
        return newsRepository.findById(news_id);
    }

    public void updateNews(String description, String main_text, MultipartFile file, Long news_id, Long newsTypeId, String newsTypeTitle,  String titleExplanation) {
        News news = newsRepository.findById(news_id).orElseThrow(EntityNotFoundException::new);
        if (file != null) {
            imageService.updateImage(file, news.getImage_id());
        }

        logger.info("news type id: " +newsTypeId);
        if (newsTypeId != null) {
            news.setNews_type_id(newsTypeId);
        }

        if (newsTypeTitle != null && titleExplanation != null) {
            Long newTypeId = createNewsType(newsTypeTitle, titleExplanation);
            news.setNews_type_id( newTypeId);
        } else {
            news.setNews_type_id(newsTypeId);
        }


        if (description != null) news.setDescription(description);
        news.setMain_text(main_text);
        news.setLast_updated(LocalDateTime.now());
        newsRepository.save(news);
    }

    public Long createNewsType(String title, String explanation) {
        newsRepository.createNewsType(title, explanation);
        return newsRepository.getLastInsertedIdOfNewsType();
    }

    public List<NewsType> getAllTypes(Long newsTypeId) {

        List<NewsType> newsTypes = newsRepository.getAllTypes();
        if (newsTypeId != null) {
            // find type of edited news and swap with element with 0 index
            int i = 0; //news id
            while (!newsTypes.get(i).getId().equals(newsTypeId)) i++;

            // swap with element with 0 index
            NewsType temp = newsTypes.get(0);
            newsTypes.set(0, newsTypes.get(i));
            newsTypes.set(i, temp);
        }
        return newsTypes;
    }
}
