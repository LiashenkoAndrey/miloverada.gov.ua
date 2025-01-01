package gov.milove.services.forum.impl;

import gov.milove.domain.media.Image;
import gov.milove.domain.dto.forum.NewForumUserDto;
import gov.milove.domain.forum.ForumUser;
import gov.milove.repositories.jpa.AppUserRepository;
import gov.milove.repositories.mongo.ImageRepo;
import gov.milove.repositories.jpa.forum.ForumUserRepo;
import gov.milove.services.forum.ForumUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Log4j2
public class ForumUserServiceImpl implements ForumUserService {

    private final AppUserRepository appUserRepo;

    private final ImageRepo imageRepo;

    private final ForumUserRepo forumUserRepo;

    @Override
    public ForumUser saveNewUser(NewForumUserDto dto, String appUserId) {
        ForumUser newForumUser = ForumUser.builder()
                .id(appUserId)
                .appUser(appUserRepo.getReferenceById(appUserId))
                .nickname(dto.getNickname())
                .aboutMe(dto.getAboutMe())
                .lastWasOnline(new Date())
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
