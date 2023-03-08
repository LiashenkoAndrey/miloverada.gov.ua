package gov.milove.domain.administration;

import gov.milove.domain.Employee;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Administration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<Employee> main_employee;


}
