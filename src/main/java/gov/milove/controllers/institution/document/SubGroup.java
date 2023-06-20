package gov.milove.controllers.institution.document;

import gov.milove.services.document.DocumentSubGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/sub-group")
public class SubGroup {

    private final DocumentSubGroupService subGroupService;

    public SubGroup(DocumentSubGroupService subGroupService) {
        this.subGroupService = subGroupService;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createNewSubGroup (
            @RequestParam("groupId") Long group_id,
            @RequestParam("title") String title) {

        subGroupService.createSubGroup(group_id, title);
        return ok("Створення успішне");
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSubGroup (@RequestParam("id") Long id) {
        boolean success = subGroupService.deleteSubGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }
}
