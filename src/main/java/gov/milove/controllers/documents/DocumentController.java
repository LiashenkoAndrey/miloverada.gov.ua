package gov.milove.controllers.documents;

import gov.milove.domain.Document;
import gov.milove.domain.dto.DocumentWithGroupDto;
import gov.milove.repositories.document.DocumentRepository;
import gov.milove.services.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
@Validated
public class DocumentController {

    private final DocumentRepository documentRepository;
    private final DocumentService documentService;

    @PutMapping("/protected/document/{id}/update")
    public Long updateDocumentName(@PathVariable Long id,
                             @NotBlank @RequestParam String name) {
        log.info("update doc = {}, name - {}", id, name);
        Document document = documentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        document.setTitle(name);
        documentRepository.save(document);
        return id;
    }

    @DeleteMapping("/protected/document/{id}/delete")
    public Document deleteDocument(@PathVariable Long id) {
        Document document = documentRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        documentService.delete(document);
        return document;
    }

    @GetMapping("/documents/search")
    public List<DocumentWithGroupDto> searchDocs(@RequestParam(name = "docName") String encodedString)  {
        return documentRepository.searchDistinctByNameContainingIgnoreCaseOrTitleContainingIgnoreCase(encodedString, encodedString);
    }

}
