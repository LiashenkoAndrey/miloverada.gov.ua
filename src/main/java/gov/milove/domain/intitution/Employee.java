package gov.milove.domain.intitution;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Institution institution;

    private String sub_institution;

    private String position;

    private String first_name;

    private String last_name;

    private String email;

    private String phone_number;
}
