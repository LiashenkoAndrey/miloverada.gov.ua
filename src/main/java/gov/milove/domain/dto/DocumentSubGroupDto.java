package gov.milove.domain.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class DocumentSubGroupDto {

    public DocumentSubGroupDto(Object[] tuples,
                            Map<String, Integer> aliasToIndexMap) {
        this.id = (Long) tuples[aliasToIndexMap.get(ID_ALIAS)];
        this.title = (String) tuples[aliasToIndexMap.get(TITLE_ALIAS)];
    }

    public static final String ID_ALIAS = "s_id";

    public static final String TITLE_ALIAS = "s_title";

    private Long id;
    private String title;
}
