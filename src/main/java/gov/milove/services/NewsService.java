package gov.milove.services;

import gov.milove.domain.CustomDate;
import gov.milove.domain.News;
import gov.milove.repositories.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    private final NewsRepository newsRepository;
    private final ImageService imageService;
    private final DocumentService documentService;

    public NewsService(NewsRepository newsRepository, ImageService imageService, DocumentService documentService) {
        this.newsRepository = newsRepository;
        this.imageService = imageService;
        this.documentService = documentService;
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
            documentService.deleteDocument(news.getImage_id());
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
}
