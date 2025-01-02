package gov.milove.services.impl;

import gov.milove.domain.forum.vote.Vote;
import gov.milove.repositories.jpa.forum.VoteCustomResponseRepo;
import gov.milove.repositories.jpa.forum.VoteRepo;
import gov.milove.repositories.jpa.forum.VoteResponseRepo;
import gov.milove.services.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class VoteServiceImpl implements VoteService {

    private final VoteRepo voteRepo;
    private final VoteCustomResponseRepo customResponseRepo;
    private final VoteResponseRepo responseRepo;

    @Override
    public Long deleteById(Long id) {
        log.info("Delete vote {}", id);
        log.info("delete custom responses...");

        customResponseRepo.deleteAllByVoteId(id);
        log.info("delete responses...");

        responseRepo.deleteAllByVoteId(id);
        log.info("Delete vote ok");
        return id;
    }

    @Override
    public Vote update(Vote vote) {
        return voteRepo.save(vote);
    }

    @Override
    public Vote create(Vote vote) {
        log.info("save a new vote....");
        return voteRepo.save(vote);
    }
}
