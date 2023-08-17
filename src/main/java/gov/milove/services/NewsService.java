package gov.milove.services;

import gov.milove.domain.dto.NewsDTO;
import gov.milove.domain.dto.NewsDtoWithImageAndType;
import org.springframework.data.domain.Page;

public interface NewsService {

    Page<NewsDTO> getPagesList(int page, int newsAmount);

    void save(NewsDtoWithImageAndType news);

    void deleteById(Long id);

    void update(NewsDtoWithImageAndType news);

}
