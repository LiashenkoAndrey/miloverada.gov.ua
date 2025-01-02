package gov.milove.repositories.jpa.forum;

import gov.milove.domain.dto.forum.PostCommentDto;
import gov.milove.domain.forum.post.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostCommentRepo extends JpaRepository<PostComment, Long> {

    @Query("select c from PostComment c where c.id = :id")
    PostCommentDto getPostDtoById(@Param("id") Long id);
}
