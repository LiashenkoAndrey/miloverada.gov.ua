package gov.milove.controllers;

import gov.milove.exceptions.ContactEmployeeException;
import gov.milove.repositories.ContactEmployeeRepository;
import gov.milove.services.ContactEmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ContactEmployee {

    private final ContactEmployeeService service;
    private final ContactEmployeeRepository repository;


    @GetMapping("/contacts")
    public List<gov.milove.domain.ContactEmployee> getAll() {
        return service.findAll();
    }


    @PostMapping("/protected/contact/new")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public gov.milove.domain.ContactEmployee create(@RequestBody gov.milove.domain.ContactEmployee employee ){
        repository.save(employee);
        return employee;
    }

    @PostMapping("/contact/update")
    public ResponseEntity<String> update (
            @RequestParam("id") Long id,
            @ModelAttribute("employee") gov.milove.domain.ContactEmployee updatedEmployee){

        try {
            gov.milove.domain.ContactEmployee oldEmployee = service.findById(id).
                    orElseThrow(EntityNotFoundException::new);
            gov.milove.domain.ContactEmployee.updateEmployee(updatedEmployee, oldEmployee);

            service.save(oldEmployee);
            return ok("Працівник успішно оновлений");
        } catch (ContactEmployeeException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з оновленням");
        }
    }

    @DeleteMapping("/protected/contact/{id}/delete")
    public Long delete(@PathVariable Long id) {
        repository.deleteById(id);
        return id;
    }
}
