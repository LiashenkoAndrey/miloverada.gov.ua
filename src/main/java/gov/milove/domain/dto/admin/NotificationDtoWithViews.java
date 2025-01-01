package gov.milove.domain.dto.admin;

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
