package gov.milove.domain.institution;


import gov.milove.domain.document.DocumentGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@ToString
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Table(name = "institution")
public class Institution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String iconUrl;

    @OneToMany(fetch = FetchType.EAGER)
    private List<DocumentGroup> document_group;

    @OneToMany
    @JoinColumn(name = "institution_id")
    private List<InstitutionEmployee> employee_list;
}
