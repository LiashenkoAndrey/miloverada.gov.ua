package gov.milove.domain.dto;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDto {



    private Long id;
    private String title;
    private String iconUrl;
}
