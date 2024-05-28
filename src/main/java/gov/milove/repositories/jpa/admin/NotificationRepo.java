package gov.milove.repositories.jpa.admin;

import gov.milove.domain.adminNotification.Notification;
import gov.milove.domain.adminNotification.NotificationDtoWithViews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

    @Query(value = "select (select count(*) from admin_notification) - ( select count(*)  from notification_view where user_id = :userId)", nativeQuery = true)
    Long getTotalNumberOfActualNotifications(@Param("userId") String userId);

    @Query("select n.message as message, n.id as id, (select count(*) > 0 from NotificationView v where v.userId = :userId and v.notification_id = n.id) as isViewed from Notification n order by n.createdOn desc ")
    List<NotificationDtoWithViews> getWithViews(@Param("userId") String userId);
}
