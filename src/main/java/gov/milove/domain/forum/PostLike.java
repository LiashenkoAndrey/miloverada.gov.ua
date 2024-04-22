package gov.milove.domain.forum;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "forum", name = "post_likes")
public class PostLike {

    public PostLike(ForumUser user, Post post) {
        this.user = user;
        this.post = post;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ForumUser user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
}
