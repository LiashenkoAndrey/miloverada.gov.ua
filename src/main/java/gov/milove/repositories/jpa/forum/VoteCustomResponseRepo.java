package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.VoteCustomResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface VoteCustomResponseRepo extends JpaRepository<VoteCustomResponse, Long> {

    @Transactional
    @Modifying
    void deleteAllByVoteId(Long voteId);
}
