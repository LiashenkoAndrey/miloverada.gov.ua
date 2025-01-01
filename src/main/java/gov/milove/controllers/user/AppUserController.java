package gov.milove.controllers.user;

import gov.milove.domain.user.AdminMetadata;
import gov.milove.domain.user.AppUser;
import gov.milove.domain.user.User;
import gov.milove.domain.dto.admin.AdminMetadataDto;
import gov.milove.domain.dto.UserDto;
import gov.milove.domain.dto.forum.AppUserDto;
import gov.milove.repositories.jpa.AppUserRepository;
import gov.milove.repositories.mongo.AdminMetadataRepo;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;

@Log4j2
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserRepository appUserRepo;
    private final AdminMetadataRepo adminMetadataRepo;

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/users")
    public List<User> getUsers() {
        List<User> users = em.createQuery("SELECT a FROM User a", User.class).getResultList();
        log.info("users = {}", users);
        return users;
    }

    @PostMapping("/protected/user/new")
    public String newAppUser(@Valid @RequestBody AppUserDto dto) {
        log.info("new user: " + dto);

        AppUser appUser = AppUser.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .avatarContentType(dto.getAvatarContentType())
                .avatarUrl(dto.getAvatarUrl())
                .avatarBase64Image(dto.getBase64Avatar())
                .build();

        log.info("user - {}" , appUser);
        AppUser saved = appUserRepo.save(appUser);
        return saved.getId();
    }

    @GetMapping("/protected/user/isRegistered/id/{encodedUserId}")
    public UserDto isRegistered(@PathVariable String encodedUserId, @RequestParam Boolean isAdmin) {
        String userId = decodeUriComponent(encodedUserId);

        AdminMetadata adminMetadata = null;

        if (isAdmin) {
            log.info("is Admin, get meta...");
            adminMetadata = adminMetadataRepo.findById(userId)
                    .orElse(adminMetadataRepo.save(AdminMetadata.builder()
                            .isDocumentsPageTourCompleted(false)
                            .isShowConfirmWhenDeleteDocument(true)
                            .isShowModalTourWhenUserOnDocumentsPage(true)
                            .userId(userId)
                            .build()));
        }

        log.info("admin meta = {}", adminMetadata);
        Boolean isExist = appUserRepo.existsById(userId);
        log.info("user id - {}, isExist - {}", userId, isExist);
        AppUser appUser =  appUserRepo.findById(encodedUserId).orElse(null);
        log.info(appUser);
        return new UserDto(appUser != null ,adminMetadata, appUser);
    }

    @GetMapping("/protected/user/id/{id}")
    public AppUser getUserById(@PathVariable String id) {
        return appUserRepo.findById(id).orElseThrow(EntityExistsException::new);
    }

    @GetMapping("/protected/appUser/{id}")
    public UserDto getUserMetaById(@PathVariable String id) {
        log.info("user id {}", id);
        AdminMetadata adminMetadata = adminMetadataRepo.findById(id).orElse(null);
        AppUser appUser = appUserRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        return new UserDto(adminMetadata, appUser);
    }


    @PutMapping("/protected/user/adminMeta/update")
    public AdminMetadata updateAdminMeta(@RequestBody AdminMetadata adminMetadata) {
        return  adminMetadataRepo.save(adminMetadata);
    }

    @PostMapping("/protected/adminMeta")
    public AdminMetadata saveMetaData(@RequestBody AdminMetadata metadata) {
        return adminMetadataRepo.save(metadata);
    }

    @PutMapping("/protected/adminMeta/{id}/update")
    public void setIsDocumentsPageTourCompleted(@RequestBody AdminMetadataDto dto, @PathVariable String id) {
        AdminMetadata metadata = adminMetadataRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        if (dto.getIsDocumentsPageTourCompleted() != null) metadata.setIsDocumentsPageTourCompleted(dto.getIsDocumentsPageTourCompleted());
        if (dto.getIsShowConfirmWhenDeleteDocument() != null) metadata.setIsShowConfirmWhenDeleteDocument(dto.getIsShowConfirmWhenDeleteDocument());
        adminMetadataRepo.save(metadata);
    }
}
