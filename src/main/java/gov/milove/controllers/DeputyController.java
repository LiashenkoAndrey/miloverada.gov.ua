package gov.milove.controllers;

import gov.milove.domain.Deputy;
import gov.milove.exceptions.DeputyServiceException;
import gov.milove.services.DeputyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/депутати по округах")
public class DeputyController {

    private final DeputyService service;

    public DeputyController(DeputyService service) {
        this.service = service;
    }

    @GetMapping
    public String showContacts(Model model) {
        model.addAttribute("employee", new Deputy());
        model.addAttribute("employee_list", service.findAll());
        return "deputies_by_districts";
    }

    @PostMapping("/employee/new")
    public ResponseEntity<String> create(@ModelAttribute("employee") Deputy employee ){

        try {
            service.save(employee);
            return ok("Працівник успішно доданий");
        } catch (DeputyServiceException ex) {
            return error("Виникли проблеми з додаванням");
        }
    }

    @PostMapping("/employee/{employee_id}/update")
    public ResponseEntity<String> update (
            @PathVariable("employee_id") Long id,
            @ModelAttribute("employee") Deputy updatedEmployee){

        try {
            Deputy oldDeputy = service.findById(id).
                    orElseThrow(EntityNotFoundException::new);
            Deputy.updateEmployee(updatedEmployee, oldDeputy);

            service.save(oldDeputy);
            return ok("Працівник успішно оновлений");
        } catch (DeputyServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з оновленням");
        }
    }

    @GetMapping("/employee/{employee_id}/delete")
    public ResponseEntity<String> delete(@PathVariable("employee_id") Long employee_id) {
        try {
            service.deleteById(employee_id);
            return ok("Працівник успішно видалений");
        } catch (DeputyServiceException ex) {
            return error("Виникли проблеми з видаленням");
        }
    }
}
