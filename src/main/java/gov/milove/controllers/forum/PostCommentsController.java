package gov.milove.controllers.forum;

import gov.milove.domain.dto.forum.PostCommentDto;
import gov.milove.domain.forum.post.PostComment;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.PostCommentRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class PostCommentsController {

    private final PostCommentRepo commentRepo;
    private final ForumUserRepo userRepo;

    @PostMapping("/protected/forum/post/{postId}/comment/new")
    public PostCommentDto newPostComment(@RequestParam String encodedForumUserId,
                                         @RequestParam String text,
                                         @PathVariable Long postId) {
        log.info("new post comment  postId={}, text={}" ,postId, text);
        String forumUserId = decodeUriComponent(encodedForumUserId);
        PostComment comment = PostComment.builder()
                .author(userRepo.getReferenceById(forumUserId))
                .text(text)
                .build();
        PostComment saved = commentRepo.save(comment);
        return commentRepo.getPostDtoById(saved.getId());
    }
}
