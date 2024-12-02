package gov.milove.domain.forum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.Formula;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@Table(schema = "forum")
public class TopicChat extends Chat {

    public TopicChat(String name, String description, String picture) {
        this.name = name;
        this.description = description;
        this.picture = picture;
    }

    private Long topicId;

    private String name;

    private String description;

    private String picture;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForumUser creator;

    @Formula("(select count(*) from forum.message m where m.chat_id = id)")
    private Long totalMessagesAmount = 0L;

    @Formula("(select count(distinct m.sender_id) from forum.message m  where m.chat_id = id)")
    private Long totalMembersAmount = 0L;

    private String idAlias;

    @PrePersist
    private void pre() {
        this.idAlias = RandomStringUtils.randomAlphanumeric(8);
    }
}
