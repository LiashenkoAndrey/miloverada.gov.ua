package gov.milove.domain.dto.admin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminMetadataDto {

    String userId;

    Boolean isDocumentsPageTourCompleted;

    Boolean isShowConfirmWhenDeleteDocument;
}
