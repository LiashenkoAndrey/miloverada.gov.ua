package gov.milove.domain.dto;

import gov.milove.domain.NewsImage;
import gov.milove.domain.NewsType;

import java.time.LocalDate;
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
