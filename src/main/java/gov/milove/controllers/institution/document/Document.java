package gov.milove.controllers.institution.document;

import gov.milove.domain.dto.DocumentGroupDto;
import gov.milove.repositories.document.DocumentRepository;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.document.DocumentService;
import gov.milove.services.document.DocumentSubGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@RestController
@RequestMapping("/api")
public class Document {

    private final DocumentService documentService;
    private final DocumentGroupService documentGroupService;
    private final DocumentSubGroupService subGroupService;

    private final DocumentRepository documentRepository;

    public Document(@Qualifier("localCommunityDocumentService") DocumentService documentService, DocumentGroupService documentGroupService, DocumentSubGroupService subGroupService, DocumentRepository documentRepository) {
        this.documentService = documentService;
        this.documentGroupService = documentGroupService;
        this.subGroupService = subGroupService;
        this.documentRepository = documentRepository;
    }


    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> saveDocument(@RequestParam("file") MultipartFile file,
                               @RequestParam("title") String title,
                               @RequestParam("subGroupId") Long subGroupId) {

        try {
            documentService.createDocument(file,title, subGroupId);
            return ok("Документ успішно доданий");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з додаванням документу");
        }
    }

    @GetMapping("/documents")
    public List<gov.milove.domain.Document> getDocumentsBySubGroupId(@RequestParam Long subGroupId) {
        return documentRepository.findAllBySubGroupId(subGroupId);
    }

    @GetMapping("/view")
    public String displayDocument(
            @RequestParam("documentId") Long document_id,
            @RequestParam("groupId") Long group_id,
            @RequestParam("subGroupId") Long sub_group_id,
            Model model) {

        Optional<gov.milove.domain.Document> document = documentService.getDocumentById(document_id);

        if (document.isEmpty()) return "error/404";
        else {
            DocumentGroupDto groupDto = documentGroupService.getDtoById(group_id);
            String subGroupTitle = subGroupService.getTitleById(sub_group_id);
            model.addAttribute("previousPageTitle", groupDto.getTitle());
            model.addAttribute("previousPageUrl", "/group/" + group_id);
            model.addAttribute("subGroupTitle", subGroupTitle);
            model.addAttribute("document", document.get());
            return "document";
        }
    }

}
