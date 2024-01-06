package gov.milove.domain.dto.forum;

import gov.milove.domain.forum.ForumUser;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class NewUserDto {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    public static ForumUser toDomain(NewUserDto dto) {
        return new ForumUser(dto.getId(), dto.getFirstName(), dto.getLastName(), dto.getEmail(), dto.getAvatar());
    }

}
