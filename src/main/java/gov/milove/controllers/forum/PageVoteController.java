package gov.milove.controllers.forum;

import gov.milove.domain.forum.vote.Vote;
import gov.milove.repositories.jpa.forum.VoteRepo;
import gov.milove.services.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class PageVoteController {

    private final VoteService voteService;
    private final VoteRepo voteRepo;

    @PostMapping("/protected/forum/pageVote/new")
    public Long createNewVote(@RequestBody Vote vote) {
        log.info("Create a new vote {}", vote);
        vote.setAuthorId(decodeUriComponent(vote.getAuthorId()));
        return voteService.create(vote)
                .getId();
    }

    /**
     * Gets latest vote
     * If no votes return null
     * @return null
     */
    @GetMapping("/forum/pageVote/latest")
    public Vote getLatestVote() {
        return voteRepo.getLatestPageVote()
                .orElse(null);
    }

}
