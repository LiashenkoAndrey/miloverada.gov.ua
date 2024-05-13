package gov.milove.domain;

import gov.milove.domain.dto.INewsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class NewsPageDto {

    public NewsPageDto(Page<INewsDto> page) {
        this.news = page.toList();
        this.totalElements = page.getTotalElements();
    }

    private List<INewsDto> news;

    private Long totalElements;
}
