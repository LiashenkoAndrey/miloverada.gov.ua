package gov.milove.domain.administration;


import gov.milove.domain.institution.Employee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "administration_employee")
public class AdministrationEmployee extends Employee {

    private String image_id;

    @ManyToOne
    private AdministrationGroup administration_group;
}
