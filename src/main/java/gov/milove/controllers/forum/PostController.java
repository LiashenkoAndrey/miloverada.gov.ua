package gov.milove.controllers.forum;


import gov.milove.domain.Image;
import gov.milove.domain.forum.Post;
import gov.milove.repositories.ImageRepo;
import gov.milove.repositories.forum.ForumUserRepo;
import gov.milove.repositories.forum.PostRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostRepo postRepo;
    private final ForumUserRepo forumUserRepo;
    private final ImageRepo imageRepo;

    @GetMapping("/forum/posts/latest")
    public List<Post> latest() {
        return postRepo.findAll(Sort.by("createdOn").descending());
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
