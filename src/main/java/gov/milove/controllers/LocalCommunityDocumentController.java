package gov.milove.controllers;

import gov.milove.domain.Document;
import gov.milove.domain.DocumentGroup;
import gov.milove.domain.dto.DocumentGroupDto;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.document.DocumentService;
import gov.milove.services.document.DocumentSubGroupService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/group")
public class LocalCommunityDocumentController {

    private final DocumentService documentService;
    private final DocumentGroupService documentGroupService;
    private final DocumentSubGroupService subGroupService;

    public LocalCommunityDocumentController(@Qualifier("localCommunityDocumentService") DocumentService documentService, DocumentGroupService documentGroupService, DocumentSubGroupService subGroupService) {
        this.documentService = documentService;
        this.documentGroupService = documentGroupService;
        this.subGroupService = subGroupService;
    }


    @PostMapping("/new")
    public String createNewGroup(@RequestParam("title") String title) {
        documentGroupService.createGroup(title);
        return "redirect:/";
    }




    @GetMapping("/{id}")
    public String group(@PathVariable("id") Long id, Model model) {
        Optional<DocumentGroup> documentGroup = documentGroupService.findDocumentGroupById(id);
        if (documentGroup.isPresent()) {
            model.addAttribute("group", documentGroup.get());
            model.addAttribute("sub_groups", subGroupService.findAllByGroupId(id));
            model.addAttribute("groups",documentGroupService.findAll());
            return "group";
        } else {
            return "error/404";
        }
    }

    @GetMapping("/{group_id}/delete")
    public ResponseEntity<String> deleteGroup (@PathVariable("group_id") Long id) {
        boolean success = documentGroupService.deleteGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }


    @PostMapping("/{group_id}/sub-group/{subGroupId}/document/new")
    public ResponseEntity<String> saveDocument(@RequestParam("file") MultipartFile file,
                               @RequestParam("title") String title,
                               @PathVariable Long subGroupId,
                               @PathVariable("group_id") String group_id) {

        try {
            documentService.createDocument(file,title, subGroupId);
            return ok("Документ успішно доданий");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням документу");
        }
    }


    @GetMapping("/{group_id}/sub-group/{sub_group_id}/document/{document_id}")
    public String displayDocument(
            @PathVariable("group_id") Long group_id,
            @PathVariable("sub_group_id") Long sub_group_id,
            @PathVariable("document_id") Long document_id,
            Model model) {
        Optional<Document> document = documentService.getDocumentById(document_id);
        if (document.isEmpty()) return "error/404";

        DocumentGroupDto groupDto = documentGroupService.getDtoById(group_id);
        String subGroupTitle = subGroupService.getTitleById(sub_group_id);
        model.addAttribute("previousPageTitle", groupDto.getTitle());
        model.addAttribute("previousPageUrl", "/group/" + group_id);
        model.addAttribute("subGroupTitle", subGroupTitle);
        model.addAttribute("document", document.get());
        return "document";
    }


    @PostMapping("/{group_id}/sub-group/new")
    public String createNewSubGroup (
            @PathVariable("group_id") Long group_id,
            @RequestParam("title") String title) {
        subGroupService.createSubGroup(group_id, title);
        return "redirect:/group/" + group_id;
    }

    @GetMapping("/sub-group/{subgroup_id}/delete")
    public ResponseEntity<String> deleteSubGroup (@PathVariable("subgroup_id") Long id) {
        boolean success = subGroupService.deleteSubGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }
}
