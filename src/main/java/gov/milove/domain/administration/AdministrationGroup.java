package gov.milove.domain.administration;

import gov.milove.domain.Employee;
import gov.milove.domain.SubGroup;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "administration_group")
public class AdministrationGroup extends SubGroup {

    @OneToMany
    private List<Employee> employeeList;
}
