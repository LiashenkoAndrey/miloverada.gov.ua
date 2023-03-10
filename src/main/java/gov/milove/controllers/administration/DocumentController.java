package gov.milove.controllers.administration;

import gov.milove.domain.Document;
import gov.milove.domain.dto.AdministrationGroupDto;
import gov.milove.exceptions.DocumentCrudServiceException;
import gov.milove.services.administration.AdministrationGroupService;
import gov.milove.services.document.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static gov.milove.controllers.ControllerUtil.error;
import static gov.milove.controllers.ControllerUtil.ok;

@Controller
@RequestMapping("/administration")
public class DocumentController {

    private final AdministrationGroupService groupService;

    private final DocumentService documentService;

    public DocumentController(AdministrationGroupService groupService, @Qualifier("administrationDocumentService") DocumentService documentService) {
        this.groupService = groupService;
        this.documentService = documentService;
    }


    @PostMapping("/group/{group_id}/document/new")
    public ResponseEntity<String> newDocument(
            @PathVariable("group_id") Long group_id,
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String doc_title) {

        try {
            assert file != null && doc_title != null;
            assert !file.isEmpty();
            documentService.createDocument(file, doc_title, group_id);

            return ok("Файл успішно доданий");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням файлу");
        }
    }


    @GetMapping("/group/{group_id}/document/{document_id}")
    public String showDocument(
            @PathVariable("group_id") Long group_id,
            @PathVariable("document_id") Long document_id,
            Model model) {

        Optional<Document> document = documentService.getDocumentById(document_id);
        if (document.isEmpty()) return "error/404";

        AdministrationGroupDto groupDto = groupService.findDtoById(group_id)
                .orElseThrow(EntityNotFoundException::new);

        model.addAttribute("previousPageTitle", "Адміністрація");
        model.addAttribute("previousPageUrl", "/administration");
        model.addAttribute("subGroupTitle", groupDto.getTitle());
        model.addAttribute("document", document.get());
        return "document";
    }

}
