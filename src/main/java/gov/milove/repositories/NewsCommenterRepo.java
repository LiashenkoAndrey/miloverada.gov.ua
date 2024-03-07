package gov.milove.repositories;

import gov.milove.domain.NewsCommenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCommenterRepo extends JpaRepository<NewsCommenter, String> {
}
