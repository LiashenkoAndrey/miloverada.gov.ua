package gov.milove.domain.adminNotification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.milove.domain.AppUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "admin_notification")
@NoArgsConstructor
public class Notification {

    public Notification(String message, String text, AppUser author) {
        this.message = message;
        this.text = text;
        this.author = author;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String message;

    private String text;

    @UpdateTimestamp
    private Date updatedOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private AppUser author;

    @CreationTimestamp
    private Date createdOn;

}
