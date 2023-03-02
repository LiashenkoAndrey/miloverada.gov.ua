package gov.milove.domain;

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

    @OneToMany(mappedBy = "document_group", cascade = CascadeType.ALL)
    private List<SubGroup> subGroups;


    public DocumentGroup() {

    }
}