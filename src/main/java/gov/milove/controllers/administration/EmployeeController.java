package gov.milove.controllers.administration;

import gov.milove.domain.Employee;
import gov.milove.domain.administration.AdministrationEmployee;
import gov.milove.services.administration.AdministrationEmployeeService;
import gov.milove.services.administration.AdministrationGroupService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@AllArgsConstructor
@RequestMapping("/administration")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private final AdministrationEmployeeService administrationEmployeeService;

    private final AdministrationGroupService administrationGroupService;


    @PostMapping("/employee/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> create(
            @ModelAttribute("main_employee") AdministrationEmployee employee,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(value = "groupId", required = false) String group_id) {

        try {
            if (group_id == null) {
//                String savedImageId = imageService.saveAndReturnId(file);
//                employee.setImage_id(savedImageId);
                administrationEmployeeService.save(employee);
            } else {
                employee.setAdministration_group(administrationGroupService.findById(Long.parseLong(group_id))
                        .orElseThrow(EntityNotFoundException::new));
                administrationEmployeeService.save(employee);
            }
            return ok("Працівник успішно доданий");
        } catch (Exception ex) {
            logger.error(ex.toString());
            return error("Виникли проблеми з додаванням");
        }
    }

    @PostMapping("/employee/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> update (
            @RequestParam("employeeId") Long id,
            @ModelAttribute("main_employee") AdministrationEmployee updatedEmployee,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            AdministrationEmployee oldEmployee =
                    administrationEmployeeService.findById(id).orElseThrow(EntityNotFoundException::new);

            if (oldEmployee.getAdministration_group() == null && file != null) {
//                String savedImageId = imageService.saveAndReturnId(file);
//                oldEmployee.setImage_id(savedImageId);
            }
            Employee.updateEmployee(updatedEmployee, oldEmployee);

            administrationEmployeeService.save(oldEmployee);
            return ok("Працівник успішно оновлений");
        } catch (Exception ex) {
            logger.error(ex.toString());
            return error("Виникли проблеми з оновленням");
        }
    }

    @GetMapping("/employee/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> delete(@RequestParam("employeeId") Long employee_id) {
        try {
            administrationEmployeeService.deleteById(employee_id);
            return ok("Працівник успішно видалений");
        } catch (Exception ex) {
            logger.error(ex.toString());
            return error("Виникли проблеми з видаленням");
        }
    }
}
