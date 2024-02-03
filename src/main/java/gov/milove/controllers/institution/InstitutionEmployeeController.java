package gov.milove.controllers.institution;

import gov.milove.domain.Employee;
import gov.milove.domain.institution.Institution;
import gov.milove.domain.institution.InstitutionEmployee;
import gov.milove.exceptions.EmployeeServiceException;
import gov.milove.services.institution.InstitutionEmployeeService;
import gov.milove.services.institution.InstitutionService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/institution")
public class InstitutionEmployeeController {


    private final InstitutionService institutionService;

    private final InstitutionEmployeeService institutionEmployeeService;

    public InstitutionEmployeeController(InstitutionService institutionService, InstitutionEmployeeService institutionEmployeeService) {
        this.institutionService = institutionService;
        this.institutionEmployeeService = institutionEmployeeService;
    }

//    @PostMapping("/employee/new")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<String> newEmployee(
//            @RequestParam("institutionId") Long institution_id,
//            @ModelAttribute("employee") InstitutionEmployee employee) {
//        try {
//            employee.setInstitution_id(institution_id);
//            institutionEmployeeService.save(employee);
//            return new ResponseEntity<>("Працівник успішно доданий", HttpStatus.OK);
//        } catch (EmployeeServiceException ex) {
//            ex.printStackTrace();
//            return error("Виникли проблеми з додаванням");
//        }
//    }


    @GetMapping("/employee/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteEmployee(@RequestParam("employeeId") Long employee_id) {
        try {
            institutionEmployeeService.deleteById(employee_id);
            return ok("Працівник успішно видалений");
        } catch (EmployeeServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з видаленням");
        }
    }


//    @PostMapping("/employee/update")
//    @PreAuthorize("hasAuthority('ADMIN')")
//    public ResponseEntity<String> updateEmployee(
//            @RequestParam("id") Long employee_id,
//            @ModelAttribute("employee") InstitutionEmployee employee) {
//
//        try {
//            InstitutionEmployee oldEmployee = institutionEmployeeService.findById(employee_id)
//                    .orElseThrow(EntityNotFoundException::new);
//
//            Employee.updateEmployee(employee, oldEmployee);
//            oldEmployee.setSub_institution(employee.getSub_institution());
//            employee.setInstitution(oldEmployee.getInstitution());
//            employee.setId(employee_id);
//            institutionEmployeeService.save(oldEmployee);
//            return ok("Данні працівника успішно оновленні");
//        } catch (EmployeeServiceException ex) {
//            ex.printStackTrace();
//            return error("Виникли проблеми з оновленням");
//        }
//    }
}
