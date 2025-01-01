package gov.milove.domain.forum.post;

import gov.milove.domain.forum.ForumUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "forum", name = "post_comments")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String text;

    private Date createdOn;

//    @ManyToOne
//    @JoinColumn(name = "post_id")
    private Long post_id;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private ForumUser author;
}
