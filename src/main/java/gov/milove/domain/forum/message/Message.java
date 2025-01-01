package gov.milove.domain.forum.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.milove.domain.dto.forum.FileDto;
import gov.milove.domain.forum.ForumUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Table(schema = "forum")
public class Message {
    public Message(String text) {
        this.text = text;
    }


    public Message(Long chatId, ForumUser sender) {
        this.chatId = chatId;
        this.sender = sender;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ManyToOne(fetch = FetchType.EAGER)
    private ForumUser sender;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id")
    private List<MessageImage> imagesList;


    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "message_id")
    private List<MessageFile> filesList;

    @Transient
    private List<FileDto> fileDtoList;

    @CreationTimestamp
    private Date createdOn;

    private Long chatId;

    @UpdateTimestamp
    private Date editedOn;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "replied_message", schema = "forum")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Message repliedMessage;


    @OneToOne(fetch = FetchType.EAGER,  cascade = CascadeType.PERSIST, mappedBy = "forwardedMessage")
//    @JoinTable(name = "forwarded_message", schema = "forum")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ForwardedMessage forwardedMessage;

}
