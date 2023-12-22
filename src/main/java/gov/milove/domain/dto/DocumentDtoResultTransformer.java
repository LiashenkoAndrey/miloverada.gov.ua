package gov.milove.domain.dto;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.hibernate.transform.ResultTransformer;

import java.util.LinkedHashMap;
import java.util.Map;

@Log4j2
@Getter
public class DocumentDtoResultTransformer implements ResultTransformer {

    private Map<Long, DocumentGroupDto> documentGroupDtoMap = new LinkedHashMap<>();

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Map<String, Integer> aliasToIndexMap = aliasToIndexMap(aliases);

        Long postId = (Long) tuple[aliasToIndexMap.get(DocumentGroupDto.ID_ALIAS)];

        DocumentGroupDto documentGroupDto = documentGroupDtoMap.computeIfAbsent(
                postId,
                id -> new DocumentGroupDto(tuple, aliasToIndexMap)
        );

        documentGroupDto.subGroups.add(
                new DocumentSubGroupDto(tuple, aliasToIndexMap)
        );
        return null;
    }

    public  Map<String, Integer> aliasToIndexMap(
            String[] aliases) {

        Map<String, Integer> aliasToIndexMap = new LinkedHashMap<>();

        for (int i = 0; i < aliases.length; i++) {
            aliasToIndexMap.put(aliases[i], i);
        }

        return aliasToIndexMap;
    }
}
