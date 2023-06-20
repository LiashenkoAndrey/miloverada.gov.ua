package gov.milove.repositories;

import gov.milove.domain.News;
import gov.milove.domain.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NewsRepository extends JpaRepository<News, Long> {

    @Query(
            "select new gov.milove.domain.dto.NewsDTO(n.id, n.description, n.image_id, n.created) FROM News n"
    )
    Page<NewsDTO> getPageOfDTO(Pageable pageable);
}
