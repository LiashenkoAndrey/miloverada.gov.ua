package gov.milove.domain.dto.document;

import java.util.List;

public interface DocumentGroupWithGroupsAndDocumentsDto {

    String getName();

    Long getId();

    List<DocumentGroupWithGroupsAndDocumentsDto> getGroups();

    List<DocumentDto> getDocuments();
}
