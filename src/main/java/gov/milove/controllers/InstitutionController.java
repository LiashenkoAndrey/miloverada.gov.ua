package gov.milove.controllers;

import gov.milove.domain.Employee;
import gov.milove.domain.institution.Institution;
import gov.milove.exceptions.EmployeeServiceException;
import gov.milove.services.DocumentGroupService;
import gov.milove.services.EmployeeService;
import gov.milove.services.InstitutionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static gov.milove.controllers.ControllerUtil.error;
import static gov.milove.controllers.ControllerUtil.ok;

@Controller
@RequestMapping("/установа")
public class InstitutionController {

    private final InstitutionService institutionService;
    private final DocumentGroupService documentGroupService;

    private final EmployeeService employeeService;

    public InstitutionController(InstitutionService institutionService, DocumentGroupService documentGroupService, EmployeeService employeeService) {
        this.institutionService = institutionService;
        this.documentGroupService = documentGroupService;
        this.employeeService = employeeService;
    }

    @GetMapping("/{title}")
    public String getInstitution(@PathVariable("title") String title, Model model) {
        model.addAttribute("groups",documentGroupService.findAll());
        Optional<Institution> institution = institutionService.findInstitutionByTitle(title);
        if (institution.isPresent()) {
            model.addAttribute("institution", institution.get());
            model.addAttribute("employee_list", employeeService.findAllByInstitutionTitle(title));
            model.addAttribute("employee", new Employee());
            return "institution";
        } else {
            return "error/404";
        }
    }


    @PostMapping("/{institution_id}/employee/new")
    public ResponseEntity<String> newEmployee(
            @PathVariable("institution_id") Long institution_id,
            @ModelAttribute("employee") Employee employee) {
        try {
            Institution institution = institutionService.findInstitutionById(institution_id)
                    .orElseThrow(EntityNotFoundException::new);

            employee.setInstitution(institution);
            employeeService.save(employee);
            return ok("Працівник успішно доданий");
        } catch (EmployeeServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням");
        }
    }


    @GetMapping("/employee/{id}/delete")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employee_id) {
        try {
            employeeService.deleteById(employee_id);
            return ok("Працівник успішно видалений");
        } catch (EmployeeServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з видаленням");
        }
    }


    @PostMapping("/employee/{id}/update")
    public ResponseEntity<String> updateEmployee(
            @PathVariable("id") Long employee_id,
            @ModelAttribute("employee") Employee employee) {

        try {
            Employee employeeFromDB = employeeService.findById(employee_id).orElseThrow(EntityNotFoundException::new);
            employee.setInstitution(employeeFromDB.getInstitution());
            employee.setId(employee_id);
            employeeService.save(employee);
            return ok("Данні працівника успішно оновленні");
        } catch (EmployeeServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з оновленням");
        }
    }
}
