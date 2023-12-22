package gov.milove.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "sub_group")
public class SubGroup {
    public SubGroup(String title, DocumentGroup document_group, Date date_of_creation) {
        this.title = title;
        this.document_group = document_group;
        this.date_of_creation = date_of_creation;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 255)
    private String title;

    @ManyToOne
    @JoinColumn(name = "document_group_id")
    private DocumentGroup document_group;

    @OneToMany(mappedBy = "sub_group", cascade = CascadeType.ALL)
    private List<Document> documents;

    private Date date_of_creation;

    public SubGroup() {

    }
}
