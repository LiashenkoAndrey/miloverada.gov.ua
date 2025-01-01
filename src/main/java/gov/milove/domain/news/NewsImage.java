package gov.milove.domain.news;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsImage {

    public NewsImage(String fileName, String mongoImageId) {
        this.fileName = fileName;
        this.mongoImageId = mongoImageId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private Long newsId;

    private String mongoImageId;
}
