package gov.milove.services;

import gov.milove.domain.CustomDate;
import gov.milove.domain.News;
import gov.milove.repositories.NewsRepository;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final ImageService imageService;

    public NewsService(NewsRepository newsRepository, ImageService imageService) {
        this.newsRepository = newsRepository;
        this.imageService = imageService;
    }


    public Page<News> getPagesList(int page) {
        return newsRepository.findAll(PageRequest.of(page,9));
    }

    public void saveNews(String description, String main_text, MultipartFile image) {
        String savedImageId = imageService.saveImage(image);
        News news = new News();
        news.setImage_id(savedImageId);
        news.setMain_text(main_text);
        news.setDescription(description);
        news.setDate_of_creation(new CustomDate());
        newsRepository.save(news);
        System.out.println("News saved!!!");
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
        newsRepository.save(news);
    }
}
