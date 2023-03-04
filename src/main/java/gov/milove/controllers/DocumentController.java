package gov.milove.controllers;

import gov.milove.domain.Document;
import gov.milove.domain.DocumentGroup;
import gov.milove.domain.dto.DocumentGroupDto;
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
    private final DocumentSubGroupService subGroupService;

    public DocumentController(DocumentService documentService,
                              DocumentGroupService documentGroupService,
                              DocumentSubGroupService subGroupService) {
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
        if (success) return new ResponseEntity<>("Видалення успішне", HttpStatus.OK);
        else return new ResponseEntity<>("Виникли проблеми з видаленням", HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @PostMapping("/{group_id}/sub-group/{subGroupId}/document/new")
    public String saveDocument(@RequestParam("file") MultipartFile file,
                               @RequestParam("title") String title,
                               @PathVariable Long subGroupId,
                               @PathVariable("group_id") String group_id) {

        documentService.createDocument(file,title, subGroupId);
        return "redirect:/group/" + group_id;
    }

    @PostMapping("/{group_id}/document/{document_id}/update")
    public String updateDocument(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "title",required = false) String title,
            @PathVariable("document_id") String document_id,
            @PathVariable("group_id") String group_id) {
        System.out.println("TITLE: " + title);
        documentService.updateDocument(document_id, file, title);
        return "redirect:/group/" + group_id;
    }

    @GetMapping("/document/{document_id}/delete")
    public ResponseEntity<String> deleteDocument(@PathVariable("document_id") String document_id) {
        boolean success = documentService.deleteDocument(document_id);

        if (success) return new ResponseEntity<>("Видалення успішне", HttpStatus.OK);
        else return new ResponseEntity<>("Виникли проблеми з видаленням", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{group_id}/sub-group/{sub_group_id}/document/{document_id}")
    public String displayDocument(
            @PathVariable("group_id") Long group_id,
            @PathVariable("sub_group_id") Long sub_group_id,
            @PathVariable("document_id") Long document_id,
            Model model) {
        Optional<Document> document = documentService.getDocumentById(document_id);
        System.out.println(document.get().getDocument_filename());
        if (document.isEmpty()) return "error/404";
        DocumentGroupDto groupDto = documentGroupService.getDtoById(group_id);
        System.out.println(groupDto.getTitle());
        System.out.println(groupDto.getId());
        String subGroupTitle = subGroupService.getTitleById(sub_group_id);
        model.addAttribute("group", groupDto);
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
        if (success) return new ResponseEntity<>("Видалення успішне", HttpStatus.OK);
        else return new ResponseEntity<>("Виникли проблеми з видаленням", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
