package gov.milove.domain.forum.chat;

import gov.milove.domain.forum.Topic;
import jakarta.persistence.*;
import lombok.*;

//@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
//@Table(schema = "forum", name = "topic_chat")
public class ChatTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Topic topic;

    @ManyToOne
    private Chat chat;
}
