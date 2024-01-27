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

    public Chat(String name, String description, String picture) {
        this.name = name;
        this.description = description;
        this.picture = picture;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String picture;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    private ForumUser owner;

    @Formula("(select count(*) from forum.message m where m.chat_id = id)")
    private Long totalMessagesAmount;
}
