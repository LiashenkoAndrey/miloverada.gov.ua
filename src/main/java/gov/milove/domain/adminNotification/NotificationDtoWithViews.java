package gov.milove.domain.adminNotification;

import gov.milove.domain.dto.IAppUserDto;

import java.util.Date;

public interface NotificationDtoWithViews {

    String getMessage();

    Long getId();

    IAppUserDto getAuthor();

    Date getCreatedOn();

    Date getUpdatedOn();

    Boolean getIsViewed();

}
