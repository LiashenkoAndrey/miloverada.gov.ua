package gov.milove.controllers;

import gov.milove.domain.DocumentGroup;
import gov.milove.domain.SubGroup;
import gov.milove.repositories.DocumentSubGroupRepository;
import gov.milove.services.DocumentGroupService;
import gov.milove.services.DocumentService;
import gov.milove.services.DocumentSubGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/group")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentGroupService documentGroupService;

    private final DocumentSubGroupRepository documentSubGroupRepository;

    private final DocumentSubGroupService subGroupService;

    public DocumentController(DocumentService documentService, DocumentGroupService documentGroupService, DocumentSubGroupRepository documentSubGroupRepository, DocumentSubGroupService subGroupService) {
        this.documentService = documentService;
        this.documentGroupService = documentGroupService;
        this.documentSubGroupRepository = documentSubGroupRepository;
        this.subGroupService = subGroupService;
    }

    @GetMapping("/{id}")
    public String group(@PathVariable("id") Long id, Model model) {
        Optional<DocumentGroup> documentGroup = documentGroupService.findDocumentGroupById(id);
        if (documentGroup.isPresent()) {
            model.addAttribute("group", documentGroup.get());
            model.addAttribute("sub_groups", subGroupService.findAllByGroupId(id));
            return "group";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/{group_id}/sub-group/{subGroupId}/document/new")
    public String saveDocument(@RequestParam("file") MultipartFile file, @RequestParam("title") String title, @PathVariable Long subGroupId) {
        System.out.println("Saving file: " + title);
        documentService.createDocument(file,title, subGroupId);
        return "redirect:/";
    }

    @PostMapping("/new")
    public String createNewGroup(@RequestParam("title") String title) {
        documentGroupService.createGroup(title);
        return "redirect:/";
    }

    @PostMapping("/{group_id}/sub-group/new")
    public String createNewSubGroup (@PathVariable("group_id") Long id, @RequestParam("title") String title) {
        subGroupService.createSubGroup(id, title);
        return "redirect:/";
    }

    @GetMapping("/sub-group/{subgroup_id}/delete")
    public ResponseEntity<String> deleteSubGroup (@PathVariable("subgroup_id") Long id) {
        boolean status = subGroupService.deleteSubGroup(id);
        if (status) return new ResponseEntity<>("Видалення успішне", HttpStatus.OK);
        else return new ResponseEntity<>("Виникли проблеми з видаленням", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
