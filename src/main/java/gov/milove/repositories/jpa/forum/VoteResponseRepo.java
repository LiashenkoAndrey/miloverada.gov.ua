package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.vote.VoteResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface VoteResponseRepo extends JpaRepository<VoteResponse, Long> {

    @Modifying
    @Transactional
    void deleteAllByVoteId(Long voteId);
}
