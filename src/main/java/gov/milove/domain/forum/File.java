package gov.milove.domain.forum;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "forum")
@EqualsAndHashCode
@ToString
public class File {

    public File(String hashCode) {
        this.hashCode = hashCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mongoFileId;

    private Long size;

    private String name;

    private String format;

    private Boolean isLarge;

    private String hashCode;
}
