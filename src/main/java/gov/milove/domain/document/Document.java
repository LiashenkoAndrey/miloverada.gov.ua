package gov.milove.domain.document;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Table
public class Document {

    public Document(Long id, String title, String name) {
        this.id = id;
        this.title = title;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mongoId;

    @NotNull
    private String title;

    @NotNull
    private String name;

    @NotNull
    private Integer hashCode;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    @JsonIgnoreProperties("hibernateLazyInitializer")
    private DocumentGroup documentGroup;
}
