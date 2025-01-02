package gov.milove.domain.forum.vote;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(schema = "forum")
public class PageVote {

    public PageVote(Long voteId) {
        this.voteId = voteId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long voteId;
}
