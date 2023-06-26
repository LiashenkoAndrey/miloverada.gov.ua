package gov.milove.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@Component
@NoArgsConstructor
public class NewsDTO {

    private Long id;

    private String description;

    private String image_id;

    private LocalDateTime created;
}
