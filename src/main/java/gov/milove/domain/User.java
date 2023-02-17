package gov.milove.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String first_name;

    private String last_name;

    private String login;

    private String password;

    @ElementCollection
    @CollectionTable(name = "role", joinColumns = @JoinColumn(name = "user_id"))
    private List<Role> roles;
}
