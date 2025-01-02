package gov.milove.repositories.jpa.news;

import gov.milove.domain.news.NewsComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsCommentRepo extends JpaRepository<NewsComment, Long> {

    List<NewsComment> findAllByNewsIdOrderByCreatedOnDesc(Long newsId);
}
