package gov.milove.domain.forum.message;


import gov.milove.domain.forum.File;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "forum")
@ToString
@EqualsAndHashCode
public class MessageFile {

    public MessageFile(File file, Long message_id) {
        this.file = file;
        this.message_id = message_id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private File file;

    private Long message_id;
}
