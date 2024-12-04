package gov.milove.domain.dto.forum;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewForumUserDto {

   public NewForumUserDto(String aboutMe, String nickname) {
      this.aboutMe = aboutMe;
      this.nickname = nickname;
   }

   public NewForumUserDto(MultipartFile avatarImageFile, String aboutMe, String nickname) {
      this.avatarImageFile = avatarImageFile;
      this.aboutMe = aboutMe;
      this.nickname = nickname;
   }

   MultipartFile avatarImageFile;

   String googleAvatar;

   String aboutMe;

   String nickname;


}
