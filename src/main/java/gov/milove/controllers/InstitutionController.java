package gov.milove.controllers;

import gov.milove.domain.Employee;
import gov.milove.domain.institution.Institution;
import gov.milove.domain.institution.InstitutionEmployee;
import gov.milove.exceptions.EmployeeServiceException;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.institution.InstitutionEmployeeService;
import gov.milove.services.institution.InstitutionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/установа")
public class InstitutionController {

    private final InstitutionService institutionService;
    private final DocumentGroupService documentGroupService;

    private final InstitutionEmployeeService institutionEmployeeService;

    public InstitutionController(InstitutionService institutionService, DocumentGroupService documentGroupService, InstitutionEmployeeService institutionEmployeeService) {
        this.institutionService = institutionService;
        this.documentGroupService = documentGroupService;
        this.institutionEmployeeService = institutionEmployeeService;
    }

    @GetMapping("/{title}")
    public String getInstitution(@PathVariable("title") String title, Model model) {
        model.addAttribute("groups",documentGroupService.findAll());
        Optional<Institution> institution = institutionService.findInstitutionByTitle(title);
        if (institution.isPresent()) {
            model.addAttribute("institution", institution.get());
            model.addAttribute("employee_list", institutionEmployeeService.findAllByInstitutionTitle(title));
            model.addAttribute("employee", new InstitutionEmployee());
            return "institution";
        } else {
            return "error/404";
        }
    }


    @PostMapping("/{institution_id}/employee/new")
    public ResponseEntity<String> newEmployee(
            @PathVariable("institution_id") Long institution_id,
            @ModelAttribute("employee") InstitutionEmployee employee) {
        try {
            Institution institution = institutionService.findInstitutionById(institution_id)
                    .orElseThrow(EntityNotFoundException::new);

            employee.setInstitution(institution);
            institutionEmployeeService.save(employee);
            return new ResponseEntity<>("Працівник успішно доданий", HttpStatus.OK);
        } catch (EmployeeServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням");
        }
    }


    @GetMapping("/employee/{id}/delete")
    public ResponseEntity<String> deleteEmployee(@PathVariable("id") Long employee_id) {
        try {
            institutionEmployeeService.deleteById(employee_id);
            return ok("Працівник успішно видалений");
        } catch (EmployeeServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з видаленням");
        }
    }


    @PostMapping("/employee/{id}/update")
    public ResponseEntity<String> updateEmployee(
            @PathVariable("id") Long employee_id,
            @ModelAttribute("employee") InstitutionEmployee employee) {

        try {
            InstitutionEmployee oldEmployee = institutionEmployeeService.findById(employee_id).orElseThrow(EntityNotFoundException::new);

            Employee.updateEmployee(employee, oldEmployee);
            oldEmployee.setSub_institution(employee.getSub_institution());
            employee.setInstitution(oldEmployee.getInstitution());
            employee.setId(employee_id);
            institutionEmployeeService.save(oldEmployee);
            return ok("Данні працівника успішно оновленні");
        } catch (EmployeeServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з оновленням");
        }
    }
}
