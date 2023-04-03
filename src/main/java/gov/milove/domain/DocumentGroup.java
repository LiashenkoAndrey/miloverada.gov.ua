package gov.milove.domain;

import gov.milove.domain.institution.Institution;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


import java.util.List;


@Data
@Builder
@AllArgsConstructor
@Entity
@Table(name = "document_group")
public class DocumentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    private Institution institution;

    @OneToMany(mappedBy = "document_group", cascade = CascadeType.ALL)
    private List<SubGroup> subGroups;

    private boolean is_general;
    public DocumentGroup() {

    }
}
