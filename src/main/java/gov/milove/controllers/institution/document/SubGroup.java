package gov.milove.controllers.institution.document;

import gov.milove.domain.Document;
import gov.milove.repositories.document.DocumentSubGroupRepository;
import gov.milove.services.document.DocumentSubGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@RestController
@RequestMapping("/api/subGroup")
@RequiredArgsConstructor
public class SubGroup {

    private final DocumentSubGroupService subGroupService;

    private final DocumentSubGroupRepository repository;


    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> createNewSubGroup (
            @RequestParam("groupId") Long group_id,
            @RequestParam("subGroupTitle") String title) {

        subGroupService.createSubGroup(group_id, title);
        return ok("Створення успішне");
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteSubGroup (@RequestParam Long id) {
        boolean success = subGroupService.deleteSubGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }


    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateSubGroup (@RequestParam("subGroupId") Long id, @RequestParam("title") String title) {
        repository.editTitle(title, id);
        return ok("Оновлення успішне");
    }
}
