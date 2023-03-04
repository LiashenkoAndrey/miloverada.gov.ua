package gov.milove.services;

import gov.milove.domain.intitution.Employee;
import gov.milove.exceptions.EmployeeServiceException;
import gov.milove.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) throws EmployeeServiceException {
        try {
            return employeeRepository.save(employee);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EmployeeServiceException("Error when saving");
        }
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public void deleteById(Long id) throws EmployeeServiceException {
        try {
            employeeRepository.deleteById(id);
        } catch (Exception ex) {
            throw new EmployeeServiceException("Employee with id:" + id + ", does not exists.");
        }
    }

    public List<Employee> findAllByInstitutionTitle(String title) {
        return employeeRepository.findByInstitution_Title(title);
    }
}
