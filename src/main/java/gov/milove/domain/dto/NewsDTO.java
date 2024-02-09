package gov.milove.domain.dto;

import gov.milove.domain.NewsImage;
import gov.milove.domain.NewsType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Component
@NoArgsConstructor
public class NewsDTO {




    public NewsDTO(List<NewsImageDTO> images) {
        this.images = images;
    }

    private Long id;

    private String description;

    private List<NewsImageDTO> images;

    private LocalDate created;

    private String newsType;

    private Long views;
}
