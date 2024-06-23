package gov.milove.domain.dto.forum;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class UpdateUserOnlineStatusDto {

    private String userIdThatOnlineStatusNeedsToBeUpdated;

    private String userIdThatNeedsNotification;

    private Boolean isOnline;

    private Date date;
}
