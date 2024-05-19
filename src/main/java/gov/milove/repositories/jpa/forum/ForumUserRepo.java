package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.ForumUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ForumUserRepo extends JpaRepository<ForumUser, String> {


    @Query("select count(*) from ForumUser")
    Integer getActiveUsersAmount();
}
