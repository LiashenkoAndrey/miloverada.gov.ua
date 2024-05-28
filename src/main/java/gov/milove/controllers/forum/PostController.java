package gov.milove.controllers.forum;


import gov.milove.domain.Image;
import gov.milove.domain.dto.forum.PostDto;
import gov.milove.domain.forum.Post;
import gov.milove.domain.forum.PostLike;
import gov.milove.repositories.mongo.ImageRepo;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.PostLikeRepo;
import gov.milove.repositories.jpa.forum.PostRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
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
        log.info("user id {}", encodedForumUserId );
        if (encodedForumUserId != null) {
            String forumUserId = decodeUriComponent(encodedForumUserId);
            return postRepo.findPostWithLikesAndUserLikeInfo(forumUserId);
        }
        return postRepo.findPostWithLikesInfo();
    }


    @PutMapping("/protected/forum/post/{postId}/likeOrDislike")
    private Long newUserPost(@RequestParam String encodedForumUserId,
                             @PathVariable Long postId) {
        String decodedForumUserId = decodeUriComponent(encodedForumUserId);
        PostLike postLike = new PostLike(forumUserRepo.getReferenceById(decodedForumUserId), postRepo.getReferenceById(postId));
        Example<PostLike> postLikeExample = Example.of(postLike);
        Optional<PostLike> postLikeOptional = postLikeRepo.findOne(postLikeExample);

        // if user liked the post - remove record,
        if (postLikeOptional.isPresent()) {
            postLikeRepo.delete(postLikeOptional.get());
        } else {
            postLikeRepo.save(postLike);
            // else create new one
        }
        return postId;
    }


    @PostMapping("/protected/forum/user/{userId}/post/new")
    private Post newUserPost(@PathVariable String userId,
                               @RequestParam MultipartFile image,
                               @RequestParam String text) {

        Image savedImage = imageRepo.save(new Image(image));
        Post saved = postRepo.save(Post.builder()
                .author(forumUserRepo.getReferenceById(userId))
                .text(text)
                .imageId(savedImage.getId())
                .build());
        return postRepo.findById(saved.getId()).orElseThrow(EntityNotFoundException::new);
    }
}
