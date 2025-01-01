package gov.milove.repositories.jpa.forum;

import gov.milove.domain.forum.vote.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface VoteRepo extends JpaRepository<Vote, Long> {

    @Query("from Vote v inner join PageVote pv on v.id = pv.voteId order by v.createdOn desc limit 1")
    Optional<Vote> getLatestPageVote();
}
