package gov.milove.controllers.institution.document;

import gov.milove.domain.DocumentGroup;
import gov.milove.repositories.document.DocumentGroupRepository;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.document.DocumentSubGroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/institution/{inst_title}/group")
public class Group {

    private final DocumentGroupService documentGroupService;
    private final DocumentSubGroupService subGroupService;

    private final DocumentGroupRepository doc_group_repo;


    public Group(DocumentGroupService documentGroupService, DocumentSubGroupService subGroupService, DocumentGroupRepository doc_group_repo) {
        this.documentGroupService = documentGroupService;
        this.subGroupService = subGroupService;
        this.doc_group_repo = doc_group_repo;
    }

    @GetMapping("/{group_id}")
    public String group(@PathVariable("group_id") Long group_id,
                        Model model) {
        Optional<DocumentGroup> documentGroup = documentGroupService.findDocumentGroupById(group_id);
        if (documentGroup.isPresent()) {

            model.addAttribute("group", documentGroup.get());
//            model.addAttribute("sub_groups", subGroupService.findAllByGroupId(group_id));
            model.addAttribute("groups",documentGroupService.findAll());
            return "group";
        } else {
            return "error/404";
        }
    }


    @PostMapping("/new")
    public String createNewGroup(@RequestParam("title") String title, @PathVariable("inst_title") String inst_title) {
        documentGroupService.createGroup(title);
        return "redirect:/";
    }

    @PostMapping("/{group_id}/update")
    public ResponseEntity<String> edit(@RequestParam("title") String title, @PathVariable("group_id") Long id) {
        doc_group_repo.updateTitle(title, id);
        return ok("Оновлення успішне");
    }


    @GetMapping("/{group_id}/delete")
    public ResponseEntity<String> deleteGroup (@PathVariable("group_id") Long id) {
        boolean success = documentGroupService.deleteGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }

}
