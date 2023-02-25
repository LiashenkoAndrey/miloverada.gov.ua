package gov.milove.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String main_text;

    private String image_id;

    @Embedded
    private CustomDate date_of_creation;
}
