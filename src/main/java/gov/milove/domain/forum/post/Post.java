package gov.milove.domain.forum.post;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.milove.domain.forum.ForumUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "forum")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private ForumUser author;

    @NotNull
    private String text;

    private String imageId;

    @OneToMany
    @JoinColumn(name = "post_id")
    private List<PostComment> comments;

    @CreationTimestamp
    private Date createdOn;
}
