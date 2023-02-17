package gov.milove.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    private String description;

    private String mainText;

    @Embedded
    private CustomDate dateOfCreation;
}
