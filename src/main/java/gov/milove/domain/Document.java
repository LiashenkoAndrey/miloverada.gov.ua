package gov.milove.domain;


import gov.milove.domain.administration.AdministrationGroup;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Document {

    public Document(Long id, String title, String document_filename) {
        this.id = id;
        this.title = title;
        this.document_filename = document_filename;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String document_filename;

    @ManyToOne
    @JoinColumn(name = "sub_group_id")
    private SubGroup sub_group;

    @ManyToOne
    private AdministrationGroup administration_group;

    public Document() {

    }
}
