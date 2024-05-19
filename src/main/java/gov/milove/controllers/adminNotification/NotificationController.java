package gov.milove.controllers.adminNotification;

import gov.milove.domain.adminNotification.Notification;
import gov.milove.domain.adminNotification.NotificationDtoWithViews;
import gov.milove.domain.adminNotification.NotificationView;
import gov.milove.repositories.jpa.admin.NotificationRepo;
import gov.milove.repositories.jpa.admin.NotificationViewRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.util.Util.decodeUriComponent;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class NotificationController {

    private final NotificationRepo repo;
    private final NotificationViewRepo viewRepo;

    @GetMapping("/protected/admin/notification/totalNumber")
    public Long getTotalNumberOfActualNotifications(@RequestParam String encodedUserId) {
        String userId = decodeUriComponent(encodedUserId);
        log.info("Get total number of notifications");
        log.info("user id {}", userId);
        return repo.getTotalNumberOfActualNotifications(userId);
    }

    @GetMapping("/protected/admin/notification/all")
    public List<NotificationDtoWithViews> getAll(@RequestParam String encodedUserId) {
        String userId = decodeUriComponent(encodedUserId);
        log.info("Get All notification");
        return repo.getWithViews(userId);
    }

    @GetMapping("/protected/admin/notification/{id}")
    public Notification getById(@PathVariable Long id, @RequestParam Boolean isViewed, @RequestParam String encodedUserId) {
        if (!isViewed) {
            String userId = decodeUriComponent(encodedUserId);
            log.info("notif not viewed! user = {} save view... ", userId);
            viewRepo.save(new NotificationView(id, userId));
        } else {
            log.info("is already viewed!");
        }
        return repo.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @PostMapping("/protected/admin/notification/new")
    public Notification createNew(@RequestBody Notification n) {
        Notification saved = repo.save(n);
        log.info("saved a new notification! {}", saved);
        return saved;
    }

    @DeleteMapping("/protected/admin/notification/{id}/delete")
    public Long createNew(@PathVariable Long id) {
        log.info("Delete a notification {}", id);
        repo.deleteById(id);
        return id;
    }
}
