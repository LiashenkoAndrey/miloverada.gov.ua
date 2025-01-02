package gov.milove.controllers.media;

import gov.milove.domain.notification.Notification;
import gov.milove.domain.dto.admin.NotificationDtoWithViews;
import gov.milove.domain.notification.NotificationView;
import gov.milove.domain.dto.admin.NewNotificationDto;
import gov.milove.repositories.jpa.AppUserRepository;
import gov.milove.repositories.jpa.notification.NotificationRepo;
import gov.milove.repositories.jpa.notification.NotificationViewRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@RequestMapping("/api/protected/admin/notification")
@RequiredArgsConstructor
@Log4j2
public class NotificationController {

    private final NotificationRepo repo;
    private final AppUserRepository appUserRepo;
    private final NotificationViewRepo viewRepo;

    @GetMapping("/totalNumber")
    public Long getTotalNumberOfActualNotifications(@RequestParam String encodedUserId) {
        String userId = decodeUriComponent(encodedUserId);
        log.info("Get total number of notifications");
        log.info("user id {}", userId);
        return repo.getTotalNumberOfActualNotifications(userId);
    }

    @GetMapping("/all")
    public List<NotificationDtoWithViews> getAll(@RequestParam String encodedUserId) {
        String userId = decodeUriComponent(encodedUserId);
        log.info("Get All notification");
        return repo.getWithViews(userId);
    }

    @GetMapping("/{id}")
    public Notification getById(@PathVariable Long id, @RequestParam Boolean isViewed, @RequestParam String encodedUserId) {
        if (!isViewed) {
            String userId = decodeUriComponent(encodedUserId);
            log.info("notif not viewed! user = {} save view... ", userId);
            viewRepo.save(new NotificationView(id, userId));
        } 
        return repo.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping("/new")
    public Notification createNew(@RequestBody NewNotificationDto n) {
        log.info("new notification - {}", n);
        Notification saved = repo.save(
                new Notification(
                        n.getMessage(),
                        n.getText(),
                        appUserRepo.getReferenceById(n.getAuthorId())
                )
        );

        log.info("saved a new notification! {}", saved);
        return repo.findById(saved.getId()).orElseThrow(EntityNotFoundException::new);
    }


    @PutMapping("/{id}/edit")
    public Notification createNew(@RequestBody NewNotificationDto n, @PathVariable Long id) {
        log.info("edit notification! {} id = {}", n, id);
        Notification notification = repo.findById(id).orElseThrow(EntityNotFoundException::new);
        notification.setText(n.getText());
        notification.setMessage(n.getMessage());
        return repo.save(notification);
    }

    @Transactional
    @DeleteMapping("/{id}/delete")
    public Long createNew(@PathVariable Long id) {
        log.info("Delete a notification {}", id);
        viewRepo.deleteAllByNotificationId(id);
        repo.deleteById(id);
        return id;
    }
}
