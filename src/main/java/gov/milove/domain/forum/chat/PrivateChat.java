package gov.milove.domain.forum.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.milove.domain.forum.ForumUser;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(schema = "forum")
public class PrivateChat {


    public PrivateChat(ForumUser user1, ForumUser user2, Long chat_id) {
        this.user1 = user1;
        this.user2 = user2;
        this.chat_id = chat_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "user1_id")
    private ForumUser user1;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "user2_id")
    private ForumUser user2;

    private Long chat_id;
}
