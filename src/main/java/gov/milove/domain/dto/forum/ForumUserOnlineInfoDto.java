package gov.milove.domain.dto.forum;

import lombok.Data;

@Data
public class ForumUserOnlineInfoDto {

    private String userId;

    private Boolean isOnline;
}
