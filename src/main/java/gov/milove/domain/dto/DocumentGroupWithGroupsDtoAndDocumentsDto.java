package gov.milove.domain.dto;

import java.util.List;

public interface DocumentGroupWithGroupsDtoAndDocumentsDto {

    String getName();

    Long getId();

    List<DocumentGroupWithGroupsDtoAndDocumentsDto> getGroups();

    List<DocumentDto> getDocuments();
}
