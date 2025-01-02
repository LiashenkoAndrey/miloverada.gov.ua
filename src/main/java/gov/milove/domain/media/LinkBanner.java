package gov.milove.domain.media;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "link_banner")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class LinkBanner extends Banner {

    @Builder
    public LinkBanner(Long id, LocalDate createdOn, LocalDateTime lastUpdated, String text, String url) {
        super(id, createdOn, lastUpdated);
        this.url = url;
        this.text = text;
    }

    @Size(min = 4, max = 255)
    @NotNull
    private String text;

    @URL
    @NotNull
    private String url;
}