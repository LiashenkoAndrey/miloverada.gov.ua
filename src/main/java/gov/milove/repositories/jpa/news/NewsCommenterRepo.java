package gov.milove.repositories.jpa.news;

import gov.milove.domain.news.NewsCommenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCommenterRepo extends JpaRepository<NewsCommenter, String> {
}
