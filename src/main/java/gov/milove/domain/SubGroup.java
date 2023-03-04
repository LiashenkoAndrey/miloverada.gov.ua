package gov.milove.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "sub_group")
public class SubGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "document_group_id")
    private DocumentGroup document_group;

    @OneToMany(mappedBy = "sub_group", cascade = CascadeType.ALL)
    private List<Document> documents;

    private CustomDate date_of_creation;

    public SubGroup() {

    }
}
