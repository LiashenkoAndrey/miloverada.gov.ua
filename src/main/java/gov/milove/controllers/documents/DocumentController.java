package gov.milove.controllers.documents;

import gov.milove.repositories.document.DocumentRepository;
import gov.milove.services.document.DocumentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@RestController
@RequestMapping("/api")
public class DocumentController {

    private final DocumentService documentService;
    private final DocumentRepository documentRepository;

    public DocumentController(@Qualifier("localCommunityDocumentService") DocumentService documentService, DocumentRepository documentRepository) {
        this.documentService = documentService;
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

}
