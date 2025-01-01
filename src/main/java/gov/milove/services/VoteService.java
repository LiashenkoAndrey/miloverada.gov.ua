package gov.milove.services;

import gov.milove.domain.forum.vote.Vote;

public interface VoteService {

    Long deleteById(Long id);

    Vote update(Vote vote);

    Vote create(Vote vote);
}
