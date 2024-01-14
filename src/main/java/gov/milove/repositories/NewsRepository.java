package gov.milove.repositories;

import gov.milove.domain.News;
import gov.milove.domain.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NewsRepository extends JpaRepository<News, Long> {



    @Query(
            "select new gov.milove.domain.dto.NewsDTO(n.id, n.description, n.image_id, n.created, n.newsType.title, n.views) FROM News n"
    )
    Page<NewsDTO> getPageOfDTO(Pageable pageable);

    @Query(
            "select new gov.milove.domain.dto.NewsDTO(n.id, n.description, n.image_id, n.created, n.newsType.title, n.views) " +
                    "FROM News n where n.newsType.id = :type_id and n.id <> :news_id "
    )
    Page<NewsDTO> getLastNewsDTOByNewsTypeIdWithLimit(@Param("news_id") Long news_id, @Param("type_id") Long newTypeId, Pageable pageable);


    @Query(
            "select new gov.milove.domain.dto.NewsDTO(n.id, n.description, n.image_id, n.created, n.newsType.title, n.views) from News n order by n.created desc"
    )
    Page<NewsDTO> getLatest(Pageable pageable);


    @Modifying
    @Transactional
    @Query("update News n set n.views = n.views + 1 where n.id = :newsId")
    void incrementViews(@Param("newsId") Long id);
}
