package gov.milove.domain.dto;

import gov.milove.domain.user.AdminMetadata;
import gov.milove.domain.user.AppUser;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto {

    public UserDto(AdminMetadata adminMetadata, AppUser appUser) {
        this.adminMetadata = adminMetadata;
        this.appUser = appUser;
    }

    public UserDto(Boolean isRegistered, AdminMetadata adminMetadata, AppUser appUser) {
        this.isRegistered = isRegistered;
        this.adminMetadata = adminMetadata;
        this.appUser = appUser;
    }

    Boolean isRegistered;

    AdminMetadata adminMetadata;

    AppUser appUser;
}
