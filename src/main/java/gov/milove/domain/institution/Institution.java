package gov.milove.domain.institution;


import gov.milove.domain.DocumentGroup;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToOne
    private DocumentGroup document_group;

    @OneToMany
    private List<InstitutionEmployee> employee_list;
}
