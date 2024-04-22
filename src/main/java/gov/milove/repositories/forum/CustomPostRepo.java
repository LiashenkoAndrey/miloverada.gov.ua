package gov.milove.repositories.forum;

import org.springframework.data.repository.query.Param;

public interface CustomPostRepo {
    Object findPostWithLikesInfoById( Long id);
}
