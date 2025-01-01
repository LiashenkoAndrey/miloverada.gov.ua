package gov.milove.domain.administration;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "administration_group")
public class AdministrationGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @OneToMany(mappedBy = "administration_group")
    private List<AdministrationEmployee> employee_list;

//    @OneToMany(mappedBy = "administration_group")
//    private List<Document> document_list;

    @ManyToOne
    private AdministrationGroup administration_group;

    @OneToMany(mappedBy = "administration_group")
    private List<AdministrationGroup> group_list;
}
