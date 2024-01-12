package gov.milove.domain.forum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(schema = "forum")
public class Chat {

    public Chat(String name, String description, String picture, Long topicId) {
        this.name = name;
        this.description = description;
        this.picture = picture;
        this.topicId = topicId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String picture;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @OneToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumUser owner;

    @JoinColumn(name = "topic_id")
    private Long topicId;


    @Formula("(select count(*) from forum.message m where m.chat_id = id)")
    private Long totalMessagesAmount;
}
