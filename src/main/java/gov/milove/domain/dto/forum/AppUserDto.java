package gov.milove.domain.dto.forum;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    @NotNull
    private String id;

    @NotNull
    private String firstName;

    private String lastName;

    private String email;

    private String avatarContentType;

    private String base64Avatar;

    @NotNull
    private String avatarUrl;
}
