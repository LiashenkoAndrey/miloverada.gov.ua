package gov.milove.repositories.jpa.notification;

import gov.milove.domain.notification.NotificationView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationViewRepo extends JpaRepository<NotificationView, Long> {

    void deleteAllByNotificationId(Long id);
}
