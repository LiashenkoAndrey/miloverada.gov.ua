package gov.milove.domain.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DocumentGroupDto {

    public static final String ID_ALIAS = "d_id";

    public static final String TITLE_ALIAS = "d_title";

    public DocumentGroupDto(String title, Long id) {
        this.title = title;
        this.id = id;
    }

    public DocumentGroupDto(Object[] tuples,
                            Map<String, Integer> aliasToIndexMap) {
        this.id = (Long) tuples[aliasToIndexMap.get(ID_ALIAS)];
        this.title = (String) tuples[aliasToIndexMap.get(TITLE_ALIAS)];
    }

    private Long id;
    private String title;
    List<DocumentSubGroupDto> subGroups = new ArrayList<>();;
}
