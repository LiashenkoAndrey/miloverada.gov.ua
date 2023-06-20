package gov.milove.domain;

import gov.milove.domain.dto.NewsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String main_text;

    private String image_id;

    private LocalDateTime created;

    private LocalDateTime last_updated;

    private Boolean is_banner;

}
