package gov.milove.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewsImageDTO {



    private Long id;

    private Long newsId;

    private Long mongoImageId;
}
