package gov.milove.controllers.institution.document;

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
@RequestMapping("/institution/{inst_title}/group/{group_id}/sub-group/{sub_group_id}/document")
public class Document {

    private final DocumentService documentService;
    private final DocumentGroupService documentGroupService;
    private final DocumentSubGroupService subGroupService;


    public Document(@Qualifier("localCommunityDocumentService") DocumentService documentService, DocumentGroupService documentGroupService, DocumentSubGroupService subGroupService) {
        this.documentService = documentService;
        this.documentGroupService = documentGroupService;
        this.subGroupService = subGroupService;
    }


    @PostMapping("/new")
    public ResponseEntity<String> saveDocument(@RequestParam("file") MultipartFile file,
                               @RequestParam("title") String title,
                               @PathVariable("sub_group_id") Long subGroupId) {

        try {
            documentService.createDocument(file,title, subGroupId);
            return ok("Документ успішно доданий");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням документу");
        }
    }


    @GetMapping("/{document_id}")
    public String displayDocument(
            @PathVariable("group_id") Long group_id,
            @PathVariable("sub_group_id") Long sub_group_id,
            @PathVariable("document_id") Long document_id,
            Model model) {

        Optional<gov.milove.domain.Document> document = documentService.getDocumentById(document_id);
        if (document.isEmpty()) return "error/404";

        DocumentGroupDto groupDto = documentGroupService.getDtoById(group_id);
        String subGroupTitle = subGroupService.getTitleById(sub_group_id);
        model.addAttribute("previousPageTitle", groupDto.getTitle());
        model.addAttribute("previousPageUrl", "/group/" + group_id);
        model.addAttribute("subGroupTitle", subGroupTitle);
        model.addAttribute("document", document.get());
        return "document";
    }

}
