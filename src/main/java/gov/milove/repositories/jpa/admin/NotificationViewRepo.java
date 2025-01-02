package gov.milove.repositories.jpa.admin;

import gov.milove.domain.adminNotification.NotificationView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationViewRepo extends JpaRepository<NotificationView, Long> {

    void deleteAllByNotificationId(Long id);
}
