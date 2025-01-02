package gov.milove.repositories.jpa.forum;

import gov.milove.domain.dto.forum.PostDto;
import gov.milove.domain.forum.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepo extends JpaRepository<Post, Long> {

    @Query("select p.id as id, p.text as text, p.imageId as imageId, p.createdOn as createdOn, p.author as author, (select count(*) from PostLike pl where pl.post.id = p.id) as likesAmount from Post p order by p.createdOn desc ")
    List<PostDto> findPostWithLikesInfo();

    @Query(""" 
            select p.id as id, 
                p.text as text, 
                p.imageId as imageId, 
                p.createdOn as createdOn, 
                p.author as author, 
                (select count(*) from PostLike pl where pl.post.id = p.id) as likesAmount, 
                (select count(*) > 0 from PostLike pl where pl.user.id = :forumUserId and pl.post.id = p.id) as isUserLikedPost,
                (select count(*) from PostComment c1 where c1.id = p.id) as commentsTotalAmount
             from Post p left join p.comments order by p.createdOn desc 
            """)
    List<PostDto> findPostWithLikesAndUserLikeInfo(@Param("forumUserId") String forumUserId);

    @Query("select p.id as id, p.text as text, p.imageId as imageId, p.createdOn as createdOn, p.author as author, (select count(*) from PostLike pl where pl.post.id = p.id) as likesAmount from Post p where p.id = :id")
    Optional<PostDto> findDtoById(@Param("id") Long id);
}


