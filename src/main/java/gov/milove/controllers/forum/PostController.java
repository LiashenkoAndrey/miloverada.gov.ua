package gov.milove.controllers.forum;


import gov.milove.domain.media.Image;
import gov.milove.domain.dto.forum.PostDto;
import gov.milove.domain.forum.post.Post;
import gov.milove.domain.forum.post.PostLike;
import gov.milove.domain.forum.post.PostLikeDto;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.PostLikeRepo;
import gov.milove.repositories.jpa.forum.PostRepo;
import gov.milove.repositories.mongo.ImageRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostRepo postRepo;
    private final ForumUserRepo forumUserRepo;
    private final ImageRepo imageRepo;
    private final PostLikeRepo postLikeRepo;

    @GetMapping("/forum/posts/latest")
    public List<PostDto> latest(@RequestParam(required = false) String encodedForumUserId) {
        if (encodedForumUserId != null) {
            String forumUserId = decodeUriComponent(encodedForumUserId);
            return postRepo.findPostWithLikesAndUserLikeInfo(forumUserId);
        }
        return postRepo.findPostWithLikesInfo();
    }


    @PutMapping("/protected/forum/post/{postId}/likeOrDislike")
    private Long likeOrDislikePost(@RequestParam String encodedForumUserId,
                             @PathVariable Long postId) {
        String decodedForumUserId = decodeUriComponent(encodedForumUserId);
        Optional<PostLikeDto> postLikeOptional = postLikeRepo.findDtoByUserAndPost(decodedForumUserId, postId);

        // if user liked the post - remove record,
        if (postLikeOptional.isPresent()) {
            postLikeRepo.deleteById(postLikeOptional.get().getId());
        } else {
            postLikeRepo.save(
                    new PostLike(
                            forumUserRepo.getReferenceById(decodedForumUserId),
                            postRepo.getReferenceById(postId)
                    )
            );
            // else create a new one
        }
        return postId;
    }


    @PostMapping("/protected/forum/user/{userId}/post/new")
    private PostDto newUserPost(@PathVariable String userId,
                               @RequestParam MultipartFile image,
                               @RequestParam String text) {

        Image savedImage = imageRepo.save(new Image(image));
        Post saved = postRepo.save(Post.builder()
                .author(forumUserRepo.getReferenceById(userId))
                .text(text)
                .imageId(savedImage.getId())
                .build());
        return postRepo.findDtoById(saved.getId()).orElseThrow(EntityNotFoundException::new);
    }

    @DeleteMapping("/protected/forum/post/{id}/delete")
    public Long deletePostById(@PathVariable Long id) {
        log.info("DELETE POST");

        PostDto postDto = postRepo.findDtoById(id).orElseThrow(EntityNotFoundException::new);
        log.info("Delete image...");
        imageRepo.deleteById(postDto.getImageId());

        log.info("Delete likes...");
        postLikeRepo.deleteAllByPostId(id);

        log.info("Delete post...");
        postRepo.deleteById(id);

        return id;
    }

}
