package gov.milove.services.impl;

import gov.milove.domain.news.News;
import gov.milove.domain.news.NewsType;
import gov.milove.domain.dto.news.NewsDtoWithImageAndType;
import gov.milove.exceptions.NewsNotFoundException;
import gov.milove.exceptions.NewsServiceException;
import gov.milove.repositories.jpa.news.NewsImageRepo;
import gov.milove.repositories.jpa.news.NewsRepository;
import gov.milove.repositories.jpa.news.NewsTypeRepo;
import gov.milove.services.news.NewsImagesService;
import gov.milove.services.news.NewsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Log4j2
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsTypeRepo newsTypeRepo;
    private final NewsImagesService imageService;
    private final NewsImageRepo newsImageRepo;

    @Override
    public News save(News news, MultipartFile[] images, LocalDateTime dateOfPostponedPublication) {
        news.setImages(imageService.saveAll(List.of(images)));

        News saved = newsRepository.save(news);
        log.info("saved news = {}", saved);
        return saved;
    }

    @Override
    public void deleteById(Long id) {
        News news = newsRepository.findById(id).orElseThrow(NewsNotFoundException::new);
        imageService.deleteAllIfNotUsed(news.getImages());
        newsRepository.delete(news);
    }

    @Override
    public void update(NewsDtoWithImageAndType news) {
        News saved = newsRepository.findById(news.getId()).orElseThrow(NewsServiceException::new);
//        imageService.updateImageIfPresent(news.getImage(), saved.getImage_id());
        news.mapToEntity(saved);
        saved.setLast_updated(LocalDateTime.now());
        defineNewsType(news, saved);
        newsRepository.save(saved);
    }

    @Override
    public void deleteNewsImageById(String mongoId) {
        imageService.deleteFromMongoIfNotUsed(mongoId);
        newsImageRepo.deleteByMongoImageId(mongoId);
    }

    private void defineNewsType(NewsDtoWithImageAndType news, News entity) {
        if (news.getNews_type_id().isEmpty()) {
            if (news.getTypeTitle() != null && news.getTitleExplanation() != null){
                entity.setNewsType(new NewsType(news.getTypeTitle(), news.getTitleExplanation()));
            }
        } else {
            long newsTypeId = Long.parseLong(news.getNews_type_id());
            if (newsTypeId == 0) entity.setNewsType(null);
            else {
                NewsType type = newsTypeRepo.findById(newsTypeId).orElseThrow(EntityNotFoundException::new);
                System.out.println(type);
                entity.setNewsType(type);
            }
        }
    }

}
