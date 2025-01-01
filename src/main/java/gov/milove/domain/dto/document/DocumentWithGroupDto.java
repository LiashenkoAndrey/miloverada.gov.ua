package gov.milove.domain.dto.document;

public interface DocumentWithGroupDto {

    Long getId();
    String getTitle();

    String getName();

    DocumentGroupDto getDocumentGroup();

}
