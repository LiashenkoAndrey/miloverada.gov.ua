package gov.milove.domain;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String urlToDocument;

    @Embedded
    private CustomDate dateOfCreation;
}
