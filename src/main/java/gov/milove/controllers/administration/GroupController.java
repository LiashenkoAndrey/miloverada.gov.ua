package gov.milove.controllers.administration;


import gov.milove.domain.administration.AdministrationGroup;
import gov.milove.exceptions.AdministrationGroupServiceException;
import gov.milove.services.administration.AdministrationGroupService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;


@Controller
@RequestMapping("administration")
public class GroupController {

    private final AdministrationGroupService service;

    public GroupController(AdministrationGroupService service) {
        this.service = service;
    }

    @PostMapping("/group/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> newGroup(@ModelAttribute("newGroup") AdministrationGroup newGroup) {
        try {
            service.save(newGroup);
            return ok("Група успішно додана");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням групи");
        }
    }

    @PostMapping("/subGroup/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> newSubGroup(
            @RequestParam("groupId") Long group_id,
            @ModelAttribute("newGroup") AdministrationGroup newGroup) {

        try {
            AdministrationGroup group = service.findById(group_id)
                    .orElseThrow(EntityNotFoundException::new);
            newGroup.setAdministration_group(group);
            service.save(newGroup);
            return ok("Підгрупа успішно додана");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням підгрупи");
        }
    }


    @PostMapping("/subGroup/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> newSubGroup(
            @RequestParam("subGroupId") Long group_id,
            @RequestParam("title") String title) {

        try {
            AdministrationGroup group = service.findById(group_id)
                    .orElseThrow(EntityNotFoundException::new);
            group.setTitle(title);
            service.save(group);
            return ok("Група успішно оновлена");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з оновленням");
        }
    }

    @GetMapping("/subGroup/delete")
    public ResponseEntity<String> deleteGroup(@RequestParam("groupId") Long group_id) {
        try {
            service.deleteById(group_id);
            return ok("Підгрупа успішно видалена");
        } catch (AdministrationGroupServiceException ex) {
            return error("Виникли проблеми з видаленням підгрупи");
        }
    }
}
