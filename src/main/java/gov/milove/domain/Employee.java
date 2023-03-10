package gov.milove.domain;

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


    // todo: use MapStruct instead!!!
    public static void updateEmployee(Employee newEmployee, Employee oldEmployee) {
        if (!newEmployee.getEmail().equals("")) oldEmployee.setEmail(newEmployee.getEmail());
        if (!newEmployee.getPosition().equals("")) oldEmployee.setPosition(newEmployee.getPosition());
        if (!newEmployee.getFirst_name().equals("")) oldEmployee.setFirst_name(newEmployee.getFirst_name());
        if (!newEmployee.getLast_name().equals("")) oldEmployee.setLast_name(newEmployee.getLast_name());
        if (!newEmployee.getPhone_number().equals("")) oldEmployee.setPhone_number(newEmployee.getPhone_number());
    }
}
