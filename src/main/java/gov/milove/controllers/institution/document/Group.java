package gov.milove.controllers.institution.document;

import gov.milove.domain.DocumentGroup;
import gov.milove.repositories.document.DocumentGroupRepository;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.document.DocumentSubGroupService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/documentGroup")
@RequiredArgsConstructor
public class Group {

    private final DocumentGroupService documentGroupService;

    private final DocumentGroupRepository doc_group_repo;


    @GetMapping("/view/{groupId}")
    public String group(@PathVariable("groupId") Long group_id,
                        Model model) {
        Optional<DocumentGroup> documentGroup = documentGroupService.findDocumentGroupById(group_id);
        if (documentGroup.isPresent()) {

            model.addAttribute("group", documentGroup.get());
            model.addAttribute("groups",documentGroupService.findAll());
            return "group";
        } else {
            return "error/404";
        }
    }


    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createNewGroup(@RequestParam("groupTitle") String title) {
        documentGroupService.createGroup(title);
        return "redirect:/";
    }

    @PostMapping("/{groupId}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> edit(@RequestParam("title") String title, @PathVariable("groupId") Long id) {
        doc_group_repo.updateTitle(title, id);
        return ok("Оновлення успішне");
    }


    @GetMapping("/delete")
    public ResponseEntity<String> deleteGroup (@RequestParam("groupId") Long id) {
        boolean success = documentGroupService.deleteGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }

}
