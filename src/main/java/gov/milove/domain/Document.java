package gov.milove.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String document_filename;

    @ManyToOne
    @JoinColumn(name = "sub_group_id")
    private SubGroup sub_group;

    public Document() {

    }
}
