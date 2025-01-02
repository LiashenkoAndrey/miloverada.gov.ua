package gov.milove.domain.forum.message;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "forum")
public class MessageImage {

    public MessageImage(String imageId, Integer hashCode) {
        this.imageId = imageId;
        this.hashCode = hashCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String imageId;

    @NotNull
    private Integer hashCode;

    private Long message_id;


    @UpdateTimestamp
    private LocalDateTime lastLoaded;
}
