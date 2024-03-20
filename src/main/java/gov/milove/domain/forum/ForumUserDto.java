package gov.milove.domain.forum;

import java.util.Date;

public interface ForumUserDto {

    String getId();

    Date getRegisteredOn();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getAvatar();

}
