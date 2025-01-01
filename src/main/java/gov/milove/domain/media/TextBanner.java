package gov.milove.domain.media;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Table(name = "text_banner")
public class TextBanner extends Banner {

    @Builder
    public TextBanner(Long id, LocalDate createdOn, LocalDateTime lastUpdated, String description, String mainText) {
        super(id, createdOn, lastUpdated);
        this.description = description;
        this.mainText = mainText;
    }

    @Size(min = 4, max = 120)
    private String description;

    @Size(max = 40000)
    private String mainText;
}
