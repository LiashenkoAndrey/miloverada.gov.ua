package gov.milove.repositories.jpa.news;

import gov.milove.domain.news.NewsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NewsTypeRepo extends JpaRepository<NewsType, Long> {


    @Modifying
    @Transactional
    @Query(value = "insert into news_type(title, title_explanation) VALUES (:newsTypeTitle, :titleExplanation); ", nativeQuery = true)
    void createNewsType(@Param("newsTypeTitle") String newsTypeTitle, @Param("titleExplanation") String titleExplanation);

    @Query(value = "SELECT currval('typeofnews_id_seq')", nativeQuery = true)
    Long getLastInsertedIdOfNewsType();

    @Query("select n.id from NewsType n where n.title = :news_type_title")
    Long getNewsTypeIdByTitle(@Param("news_type_title") String newsTypeTitle);


}
