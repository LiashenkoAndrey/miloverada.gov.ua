package gov.milove.domain.forum;

import java.util.Date;

public interface ForumUserDto {

    String getId();

    Date getRegisteredOn();

    String getNickname();

    String getAboutMe();

    String getAvatar();
}
