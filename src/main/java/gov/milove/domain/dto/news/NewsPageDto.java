package gov.milove.domain.dto.news;

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
