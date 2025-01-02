package gov.milove.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "app_users")
public class AppUser extends User {

    public AppUser(String id) {
        super(id);
    }

    @Builder
    public AppUser(String id, @NotBlank String firstName, @Email String email, String lastName, String avatarUrl, LocalDateTime registeredOn, String avatarContentType, String avatarBase64Image) {
        super(id, firstName, email);
        this.lastName = lastName;
        this.avatarUrl = avatarUrl;
        this.registeredOn = registeredOn;
        this.avatarContentType = avatarContentType;
        this.avatarBase64Image = avatarBase64Image;
    }

    @NotBlank
    private String lastName;

    @URL
    private String avatarUrl;

    @CreationTimestamp
    private LocalDateTime registeredOn;

    private String avatarContentType;

    @Column(name = "avatar_base64_image")
    private String avatarBase64Image;
}