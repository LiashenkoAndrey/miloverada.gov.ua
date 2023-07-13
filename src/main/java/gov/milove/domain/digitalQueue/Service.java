package gov.milove.domain.digitalQueue;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@Table(name = "services", schema = "queue")
public class Service {

    public Service(String name, String description, String imageId) {
        this.name = name;
        this.description = description;
        this.imageId = imageId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 4, max = 255)
    private String name;

    @NotNull
    @Size(min = 5, max = 255)
    private String description;

    @NotNull
    private String imageId;

}


