package gov.milove.domain.institution;

import jakarta.persistence.*;
import lombok.Data;

@Data
@MappedSuperclass
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    private String first_name;

    private String last_name;

    private String email;

    private String phone_number;

}
