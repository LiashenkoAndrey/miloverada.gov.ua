package gov.milove.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Immutable;

@Getter
@Immutable
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class NewsType {

    public NewsType(String title, String titleExplanation) {
        this.title = title;
        this.titleExplanation = titleExplanation;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String titleExplanation;
}
