package gov.milove.repositories.jpa.news;

import gov.milove.domain.news.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NewsImageRepo extends JpaRepository<NewsImage, Long> {

    @Query(value = "select count(id) > 1 from news_image img  where img.mongo_image_id = :imageId", nativeQuery = true)
    boolean newsImageIsUsedMoreThenOneTime(@Param("imageId") String imageId);

    @Transactional
    @Modifying
    void deleteByMongoImageId(String mongoId);
}
