package gov.milove.controllers.forum;

import gov.milove.domain.forum.Topic;
import gov.milove.repositories.TopicRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/forum/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicRepo topicRepo;

    @GetMapping("/all")
    public List<Topic> getAll() {
        return topicRepo.findAll();
    }
}
