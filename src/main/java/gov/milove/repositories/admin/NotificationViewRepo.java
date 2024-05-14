package gov.milove.repositories.admin;

import gov.milove.domain.adminNotification.NotificationView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationViewRepo extends JpaRepository<NotificationView, Long> {
}
