package gov.milove.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
@Table(name = "app_users")
public class AppUser {

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

    private String avatarContentType;

    @Column(name = "avatar_base64_image")
    private String avatarBase64Image;

    private String avatarUrl;
}