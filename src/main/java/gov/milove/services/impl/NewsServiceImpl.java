package gov.milove.services.impl;

import gov.milove.domain.News;
import gov.milove.domain.NewsType;
import gov.milove.domain.dto.NewsDTO;
import gov.milove.domain.dto.NewsDtoWithImageAndType;
import gov.milove.exceptions.NewsNotFoundException;
import gov.milove.exceptions.NewsServiceException;
import gov.milove.repositories.NewsRepository;

import gov.milove.repositories.NewsTypeRepository;
import gov.milove.services.ImageService;
import gov.milove.services.NewsService;
import gov.milove.services.NewsTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private static final Logger logger = LoggerFactory.getLogger(NewsServiceImpl.class);

    private final NewsRepository newsRepository;
    private final NewsTypeService newsTypeService;

    private final NewsTypeRepository newsTypeRepository;
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

    public void save(NewsDtoWithImageAndType news) {
        News entity = news.toEntity();
        entity.setCreated(news.getCreated().isEmpty() ? LocalDate.now() : LocalDate.parse(news.getCreated()));
        entity.setImage_id(imageService.saveAndReturnId(news.getImage()));
        defineNewsType(news, entity);
        newsRepository.save(entity);
    }


    public void deleteById(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(NewsNotFoundException::new);
        imageService.deleteImageById(news.getImage_id());
        newsRepository.delete(news);
    }

    public void update(NewsDtoWithImageAndType news) {
        News saved = newsRepository.findById(news.getId()).orElseThrow(NewsServiceException::new);
        imageService.updateImageIfPresent(news.getImage(), saved.getImage_id());
        news.mapToEntity(saved);
        saved.setLast_updated(LocalDateTime.now());
        defineNewsType(news, saved);
        newsRepository.save(saved);
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
                NewsType type = newsTypeRepository.findById(newsTypeId).orElseThrow(EntityNotFoundException::new);
                System.out.println(type);
                entity.setNewsType(type);
            }
        }
    }

}
