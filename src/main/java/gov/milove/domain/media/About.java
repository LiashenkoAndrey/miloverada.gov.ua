package gov.milove.domain.media;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "about")
public class About {

    @Id
    private String id;

    @NotNull
    private String mainText;

    @LastModifiedBy
    private LocalDateTime lastUpdated;
}
