package gov.milove.services;

import gov.milove.domain.News;
import gov.milove.domain.dto.NewsDTO;
import gov.milove.repositories.NewsRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final ImageService imageService;


    /**
     * Find news by page number and newsAmount
     * if pagesAmount == 0 then pagesAmount = 9
     * @param page number of page
     * @param newsAmount amount of news in one page
     * @return page of news
     */

    public Page<NewsDTO> getPagesList(int page, int newsAmount) {
        return newsRepository.getPageOfDTO(PageRequest.of(page, newsAmount).withSort(Sort.Direction.DESC, "created"));
    }

    public void saveNews(String description, String main_text, MultipartFile image, String date) {
        String savedImageId = imageService.saveImage(image);
        newsRepository.save(News.builder()
                        .description(description)
                        .main_text(main_text)
                        .image_id(savedImageId)
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
            throw new RuntimeException(ex);
        }
        return success;
    }

    public Optional<News> getNewsById(Long news_id) {
        return newsRepository.findById(news_id);
    }

    public void updateNews(String description, String main_text, MultipartFile file, Long news_id) {
        News news = newsRepository.findById(news_id).orElseThrow(EntityNotFoundException::new);
        if (!file.isEmpty()) {
            imageService.updateImage(file, news.getImage_id());
        }

        if (description != null) news.setDescription(description);
        if (main_text != null) news.setMain_text(main_text);
        news.setLast_updated(LocalDateTime.now());
        newsRepository.save(news);
    }
}
