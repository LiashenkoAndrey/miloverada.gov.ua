package gov.milove.controllers.administration;

import gov.milove.domain.Employee;
import gov.milove.domain.administration.AdministrationEmployee;
import gov.milove.services.administration.AdministrationEmployeeService;
import gov.milove.services.administration.AdministrationGroupService;
import gov.milove.services.ImageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/administration")
public class EmployeeController {

    private final AdministrationEmployeeService administrationEmployeeService;

    private final AdministrationGroupService administrationGroupService;

    private final ImageService imageService;

    public EmployeeController(AdministrationEmployeeService administrationEmployeeService, AdministrationGroupService administrationGroupService, ImageService imageService) {
        this.administrationEmployeeService = administrationEmployeeService;
        this.administrationGroupService = administrationGroupService;
        this.imageService = imageService;
    }



    @PostMapping("/group/{group_id}/employee/new")
    public ResponseEntity<String> create(
            @ModelAttribute("main_employee") AdministrationEmployee employee,
            @RequestParam(name = "file", required = false) MultipartFile file,
            @PathVariable("group_id") String group_id) {

        try {
            if (group_id.equals("null")) {
                System.out.println("inside!!!");
                String savedImageId = imageService.saveImage(file);
                employee.setImage_id(savedImageId);
                administrationEmployeeService.save(employee);
            } else {
                employee.setAdministration_group(administrationGroupService.findById(Long.parseLong(group_id))
                        .orElseThrow(EntityNotFoundException::new));
                administrationEmployeeService.save(employee);
            }
            return ok("Працівник успішно доданий");
        } catch (Exception ex) {
            return error("Виникли проблеми з додаванням");
        }
    }

    @PostMapping("/employee/{employee_id}/update")
    public ResponseEntity<String> update (
            @PathVariable("employee_id") Long id,
            @ModelAttribute("main_employee") AdministrationEmployee updatedEmployee,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        try {
            AdministrationEmployee oldEmployee =
                    administrationEmployeeService.findById(id).orElseThrow(EntityNotFoundException::new);

            if (oldEmployee.getAdministration_group() == null && file != null) {
                String savedImageId = imageService.saveImage(file);
                oldEmployee.setImage_id(savedImageId);
            }
            Employee.updateEmployee(updatedEmployee, oldEmployee);

            administrationEmployeeService.save(oldEmployee);
            return ok("Працівник успішно оновлений");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з оновленням");
        }
    }

    @GetMapping("/employee/{employee_id}/delete")
    public ResponseEntity<String> delete(@PathVariable("employee_id") Long employee_id) {
        try {
            administrationEmployeeService.deleteById(employee_id);
            return ok("Працівник успішно видалений");
        } catch (Exception ex) {
            return error("Виникли проблеми з видаленням");
        }
    }
}
