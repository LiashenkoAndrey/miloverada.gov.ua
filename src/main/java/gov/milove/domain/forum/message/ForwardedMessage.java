package gov.milove.domain.forum.message;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(schema = "forum", name = "forwarded_message")
public class ForwardedMessage {
//
//    public ForwardedMessage(Message forwardedMessage, Chat chat) {
//        this.forwardedMessage = forwardedMessage;
//    }


    @Id
    private Long messageId;

    @OneToOne
    @JoinColumn(name ="forwarded_message_id")
    private Message forwardedMessage;

//    @ManyToOne
//    @JoinColumn(name = "from_chat_id")
//    private Chat chat;
}
