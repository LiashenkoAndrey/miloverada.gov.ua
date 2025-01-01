package gov.milove.domain.document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "document_group")
@NoArgsConstructor
public class DocumentGroup {

    public DocumentGroup(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 1000)
    private String name;

    @ManyToOne
    @JsonIgnore
    private DocumentGroup documentGroup;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_group_id")
    @JsonIgnore
    private List<Document> documents = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_group_id")
    @JsonIgnore
    private List<DocumentGroup> groups  = new ArrayList<>();

    @CreationTimestamp
    private Date createdOn;
}
