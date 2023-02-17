package gov.milove.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "document_group")
public class DocumentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

}
