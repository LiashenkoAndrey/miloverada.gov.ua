package gov.milove.domain.dto.media;

import gov.milove.domain.news.NewsCommenter;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewCommentDto {


    private Long newsId;
    private String appUserId;
    private NewsCommenter newsCommenter;
    private Long commentId;

    @NotNull
    private String text;
}
