package gov.milove.services.forum.impl;

import gov.milove.domain.Image;
import gov.milove.domain.dto.forum.NewForumUserDto;
import gov.milove.domain.forum.ForumUser;
import gov.milove.repositories.AppUserRepo;
import gov.milove.repositories.ImageRepo;
import gov.milove.repositories.forum.ForumUserRepo;
import gov.milove.services.forum.ForumUserService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class ForumUserServiceImpl implements ForumUserService {

    private final AppUserRepo appUserRepo;

    private final ImageRepo imageRepo;

    private final ForumUserRepo forumUserRepo;

    @Override
    public ForumUser saveNewUser(NewForumUserDto dto, String appUserId) {
        ForumUser newForumUser = ForumUser.builder()
                .id(appUserId)
                .appUser(appUserRepo.getReferenceById(appUserId))
                .nickname(dto.getNickname())
                .aboutMe(dto.getAboutMe())
                .build();

        if (dto.getAvatarImageFile() != null) {
            Image savedAvatar = imageRepo.save(new Image(dto.getAvatarImageFile()));
            newForumUser.setAvatar(savedAvatar.getId());
        } else {
            newForumUser.setAvatar(dto.getGoogleAvatar());
        }
        ForumUser saved = forumUserRepo.save(newForumUser);

        log.info("New forum user was saved! {}", newForumUser);
        return saved;
    }
}
