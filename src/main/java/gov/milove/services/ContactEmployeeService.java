package gov.milove.services;


import gov.milove.domain.ContactEmployee;
import gov.milove.domain.Employee;
import gov.milove.exceptions.ContactEmployeeException;
import gov.milove.repositories.ContactEmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactEmployeeService {

    private final ContactEmployeeRepository repository;

    public ContactEmployeeService(ContactEmployeeRepository repository) {
        this.repository = repository;
    }

    public void save(ContactEmployee employee) throws ContactEmployeeException {
        try {
            repository.save(employee);
        } catch (Exception ex) {
            throw new ContactEmployeeException(ex.getMessage());
        }
    }


    public void deleteById(Long employee_id) throws ContactEmployeeException {
        try {
            repository.deleteById(employee_id);
        } catch (Exception ex) {
            throw new ContactEmployeeException(ex.getMessage());
        }
    }

    public Optional<ContactEmployee> findById(Long id) throws ContactEmployeeException {
        try {
            return repository.findById(id);
        } catch (Exception ex) {
            throw new ContactEmployeeException(ex.getMessage());
        }
    }

    public List<ContactEmployee> findAll() {
        return repository.findAll();
    }
}
