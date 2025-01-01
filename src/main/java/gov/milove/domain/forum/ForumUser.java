package gov.milove.domain.forum;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.milove.domain.user.AppUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Table(name = "forum_users", schema = "forum")
public class ForumUser {

    @Id
    private String id;

    @CreationTimestamp
    private Date registeredOn;

    private String nickname;

    private String avatar;

    private String aboutMe;

    private Boolean isOnline;

    private Date lastWasOnline;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AppUser appUser;

    private Boolean isVerified;
}
