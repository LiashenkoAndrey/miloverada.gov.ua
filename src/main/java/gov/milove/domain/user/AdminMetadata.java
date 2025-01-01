package gov.milove.domain.user;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "admin_metadata")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminMetadata {



    @Id
    @NotNull
    String userId;

    Boolean isDocumentsPageTourCompleted;

    Boolean isShowConfirmWhenDeleteDocument;

    Boolean isShowModalTourWhenUserOnDocumentsPage;
}
