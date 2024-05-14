package gov.milove.domain.adminNotification;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "admin_notification")
public class Notification {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String message;

    private String text;

    @CreationTimestamp
    private Date createdOn;
}
