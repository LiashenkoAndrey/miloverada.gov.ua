package gov.milove.domain;

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

    public NewsImage(String mongoImageId) {
        this.mongoImageId = mongoImageId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long newsId;

    private String mongoImageId;
}
