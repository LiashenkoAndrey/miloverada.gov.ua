package gov.milove.domain.dto;

import gov.milove.domain.SubGroupDto2;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class DocumentGroupDto2  {
    public DocumentGroupDto2(String title, Long id, List<SubGroupDto2> subGroups) {
        this.title = title;
        this.id = id;
        this.subGroups = subGroups;
    }

    private String title;
    private Long id;
    private List<SubGroupDto2> subGroups;
}
