package gov.milove.controllers.forum;

import gov.milove.domain.media.Image;
import gov.milove.domain.dto.forum.UserStoryDto;
import gov.milove.domain.forum.Story;
import gov.milove.repositories.mongo.ImageRepo;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.repositories.jpa.forum.StoryRepo;
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
public class StoryController {

    private final StoryRepo storyRepo;
    private final ForumUserRepo forumUserRepo;
    private final ImageRepo imageRepo;

    @GetMapping("/forum/stories/latest")
    public List<Story> latest() {
        return storyRepo.findAll(Sort.by("createdOn").descending());
    }

    @PostMapping("/protected/forum/user/{userId}/story/new")
    private UserStoryDto newUserStory(@PathVariable String userId,
                                      @RequestParam MultipartFile image,
                                      @RequestParam String text) {

        Image savedImage = imageRepo.save(new Image(image));
        Story saved = storyRepo.save(Story.builder()
                        .author(forumUserRepo.getReferenceById(userId))
                        .text(text)
                        .imageId(savedImage.getId())
                .build());
        return storyRepo.findUserStoryById(saved.getId()).orElseThrow(EntityNotFoundException::new);
    }
}
