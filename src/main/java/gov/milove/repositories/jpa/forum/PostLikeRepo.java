package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.post.PostLike;
import gov.milove.domain.forum.post.PostLikeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface PostLikeRepo extends JpaRepository<PostLike, Long> {

    @Query("select pl.id as id from PostLike pl where pl.user.id = :userId and pl.post.id = :postId")
    Optional<PostLikeDto> findDtoByUserAndPost(@Param("userId") String userId, @Param("postId") Long postId);

    @Modifying
    @Transactional
    void deleteAllByPostId(Long postId);
}
