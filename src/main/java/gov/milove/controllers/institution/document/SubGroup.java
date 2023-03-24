package gov.milove.controllers.institution.document;

import gov.milove.services.document.DocumentSubGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/institution/{inst_id}/")
public class SubGroup {

    private final DocumentSubGroupService subGroupService;

    public SubGroup(DocumentSubGroupService subGroupService) {
        this.subGroupService = subGroupService;
    }

    @PostMapping("/group/{group_id}/sub-group/new")
    public ResponseEntity<String> createNewSubGroup (
            @PathVariable("group_id") Long group_id,
            @RequestParam("title") String title) {

        subGroupService.createSubGroup(group_id, title);
        return ok("Створення успішне");
    }

    @GetMapping("/sub-group/{subgroup_id}/delete")
    public ResponseEntity<String> deleteSubGroup (@PathVariable("subgroup_id") Long id) {
        boolean success = subGroupService.deleteSubGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }
}
