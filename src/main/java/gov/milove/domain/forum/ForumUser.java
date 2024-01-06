package gov.milove.domain.forum;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "forum_users", schema = "forum")
public class ForumUser {


    public ForumUser(String id, String firstName, String lastName, String email, String avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.avatar = avatar;
    }

    @Id
    private String id;

    @CreationTimestamp
    private LocalDateTime registeredOn;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    @NotBlank
    private String avatar;

}
