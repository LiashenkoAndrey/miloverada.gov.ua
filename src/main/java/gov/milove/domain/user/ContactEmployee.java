package gov.milove.domain.user;

import gov.milove.domain.institution.Employee;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "contact_employee")
public class ContactEmployee extends Employee {
}
