package gov.milove.domain.dto.document;

public interface DocumentDto {

    Long getId();
    String getTitle();

    String getName();

    DocGroupWithOnlyId getDocumentGroup();
}
