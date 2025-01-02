package gov.milove.domain.dto.media;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BannerDto {

    private Long id;

    @Size(min = 4, max = 120)
    @NotNull
    private String description;

    @Size(max = 40000)
    @NotNull
    private String mainText;

}
