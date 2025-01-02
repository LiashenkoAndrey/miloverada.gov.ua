package gov.milove.domain.forum.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.milove.domain.forum.ForumUser;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode
@Table(schema = "forum")
public class Chat {


    public Chat(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Chat(String name, String description, String picture) {
        this.name = name;
        this.description = description;
        this.picture = picture;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String idAlias;

    private String name;

    private String description;

    private String picture;

    @CreationTimestamp
    private Date createdOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumUser owner;

    private Boolean isPrivate;

    @Formula("(select count(*) from forum.message m where m.chat_id = id)")
    private Long totalMessagesAmount = 0L;

    @Formula("(select count(distinct m.sender_id) from forum.message m  where m.chat_id = id)")
    private Long totalMembersAmount = 0L;

    @PrePersist
    private void pre() {
        this.idAlias = RandomStringUtils.randomAlphanumeric(8);
    }
}
