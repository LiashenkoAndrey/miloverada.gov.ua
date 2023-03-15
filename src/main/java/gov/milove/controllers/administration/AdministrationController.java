package gov.milove.controllers.administration;


import gov.milove.domain.administration.AdministrationEmployee;
import gov.milove.domain.administration.AdministrationGroup;
import gov.milove.services.administration.AdministrationEmployeeService;
import gov.milove.services.administration.AdministrationGroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class AdministrationController {

    private final AdministrationGroupService groupService;
    private final AdministrationEmployeeService employeeService;

    public AdministrationController(AdministrationGroupService groupService, AdministrationEmployeeService employeeService) {
        this.groupService = groupService;
        this.employeeService = employeeService;
    }


    @GetMapping("administration")
    public String getPage(Model model) {
        model.addAttribute("main_employee", new AdministrationEmployee());
        model.addAttribute("main_employees", employeeService.findAllWhereGroupIdIsNull());
        model.addAttribute("groups", groupService.findAllGroups());
        model.addAttribute("newGroup", new AdministrationGroup());
        return "administration/administration";
    }

}
