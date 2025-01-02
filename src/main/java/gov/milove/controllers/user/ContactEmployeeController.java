package gov.milove.controllers.user;

import gov.milove.domain.user.ContactEmployee;
import gov.milove.repositories.jpa.contact.ContactEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContactEmployeeController {

    private final ContactEmployeeRepository repository;


    @GetMapping("/contacts")
    public List<ContactEmployee> getAll() {
        return repository.findAll();
    }


    @PostMapping("/protected/contact/new")
    public ContactEmployee create(@RequestBody ContactEmployee employee) {
        repository.save(employee);
        return employee;
    }

    @PostMapping("/contact/update")
    public Long update(
            @RequestParam("id") Long id,
            @RequestBody ContactEmployee updatedEmployee) {

        updatedEmployee.setId(id);
        repository.save(updatedEmployee);
        return id;
    }

    @DeleteMapping("/protected/contact/{id}/delete")
    public Long delete(@PathVariable Long id) {
        repository.deleteById(id);
        return id;
    }
}
