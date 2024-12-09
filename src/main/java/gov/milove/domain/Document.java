package gov.milove.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

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
