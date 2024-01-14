package gov.milove.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 4, max = 120)
    @NotNull
    private String description;

    @Size(max = 40000)
    @NotNull
    private String main_text;

    @NotNull
    private String image_id;

    @NotNull
    private LocalDate created;

    private LocalDateTime last_updated;

    private Long views;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private NewsType newsType;
}
