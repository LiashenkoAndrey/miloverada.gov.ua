package gov.milove.controllers;

import gov.milove.exceptions.ContactEmployeeException;
import gov.milove.services.ContactEmployeeService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/контакти")
public class ContactEmployee {

    private final ContactEmployeeService service;

    public ContactEmployee(ContactEmployeeService service) {
        this.service = service;
    }

    @GetMapping
    public String showContacts(Model model) {
        model.addAttribute("employee", new gov.milove.domain.ContactEmployee());
        model.addAttribute("employee_list", service.findAll());
        return "contacts";
    }

    @PostMapping("/employee/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> create(@ModelAttribute("employee") gov.milove.domain.ContactEmployee employee ){

        try {
            service.save(employee);
            return ok("Працівник успішно доданий");
        } catch (Exception ex) {
            return error("Виникли проблеми з додаванням");
        }
    }

    @PostMapping("/employee/{employee_id}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> update (
            @PathVariable("employee_id") Long id,
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

    @GetMapping("/employee/{employee_id}/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> delete(@PathVariable("employee_id") Long employee_id) {
        try {
            service.deleteById(employee_id);
            return ok("Працівник успішно видалений");
        } catch (ContactEmployeeException ex) {
            return error("Виникли проблеми з видаленням");
        }
    }
}
