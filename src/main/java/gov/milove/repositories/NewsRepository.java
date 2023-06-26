package gov.milove.repositories;

import gov.milove.domain.News;
import gov.milove.domain.NewsType;
import gov.milove.domain.dto.NewsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {



    @Query(
            "select new gov.milove.domain.dto.NewsDTO(n.id, n.description, n.image_id, n.created) FROM News n"
    )
    Page<NewsDTO> getPageOfDTO(Pageable pageable);

    @Query("select n.id from NewsType n where n.title = :news_type_title")
    Long getNewsTypeIdByTitle(@Param("news_type_title") String newsTypeTitle);

    @Query("from NewsType t where t.id = :id ")
    NewsType getNewsTypeById(@Param("id") Long id);

    @Query(
            "select new gov.milove.domain.dto.NewsDTO(n.id, n.description, n.image_id, n.created) " +
                    "FROM News n where n.news_type_id = :type_id and n.id <> :news_id "
    )
    Page<NewsDTO> getLastNewsDTOByNewsTypeIdWithLimit(@Param("news_id") Long news_id, @Param("type_id") Long newTypeId, Pageable pageable);

    @Query("from NewsType t")
    List<NewsType> getAllTypes();

    @Modifying
    @Transactional
    @Query(value = "insert into news_type(title, title_explanation) VALUES (:newsTypeTitle, :titleExplanation); ", nativeQuery = true)
    void createNewsType(@Param("newsTypeTitle") String newsTypeTitle, @Param("titleExplanation") String titleExplanation);

    @Query(value = "SELECT currval('typeofnews_id_seq')", nativeQuery = true)
    Long getLastInsertedIdOfNewsType();
}
