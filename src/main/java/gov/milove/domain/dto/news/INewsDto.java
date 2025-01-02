package gov.milove.domain.dto.news;

import gov.milove.domain.news.NewsImage;
import gov.milove.domain.news.NewsType;

import java.time.LocalDateTime;
import java.util.List;

public interface INewsDto {

    String getDescription();

    LocalDateTime getDateOfPublication();

    Long getViews();

    NewsType getNewsType();

    String getImage_id();

    Long getCommentsAmount();

    List<NewsImage> getImages();

    Long getId();
}
