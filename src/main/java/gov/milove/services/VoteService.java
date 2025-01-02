package gov.milove.services;

import gov.milove.domain.forum.Vote;

public interface VoteService {

    Long deleteById(Long id);

    Vote update(Vote vote);

    Vote create(Vote vote);
}
