package gov.milove.controllers.documents;

import gov.milove.domain.document.Document;
import gov.milove.domain.dto.document.DocumentWithGroupDto;
import gov.milove.repositories.jpa.document.DocumentRepo;
import gov.milove.repositories.mongo.MongoDocumentRepo;
import gov.milove.services.document.DocumentService;
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

    private final DocumentRepo documentRepo;
    private final DocumentService documentService;
    private final MongoDocumentRepo mongoDocumentRepo;

    @PutMapping("/protected/document/{id}/update")
    public Long updateDocumentName(@PathVariable Long id,
                             @NotBlank @RequestParam String name) {
        log.info("update doc = {}, name - {}", id, name);
        Document document = documentRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        document.setTitle(name);
        documentRepo.save(document);
        return id;
    }
//
//    @GetMapping("/documents/deleteUnused")
//    public void delete() {
//        log.info("Start search");
//        List<String> documents = documentRepository.findAll().stream().map(Document::getMongoId).toList();
//        log.info("50%");
//        List<MongoDocument> mongoDocuments = mongoDocumentRepo.findAll().stream()
//                .filter(mongoDocument -> !documents.contains(mongoDocument.getId())).toList();
//        log.info("Not used docs! size = {} {}", mongoDocuments.size(), mongoDocuments);
//        mongoDocumentRepo.deleteAll(mongoDocuments);
//        log.info("End search");
//    }

    @DeleteMapping("/protected/document/{id}/delete")
    public Document deleteDocument(@PathVariable Long id) {
        Document document = documentRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        documentService.delete(document);
        return document;
    }

    @GetMapping("/documents/search")
    public List<DocumentWithGroupDto> searchDocs(@RequestParam(name = "docName") String encodedString)  {
        return documentRepo.searchDistinctByNameContainingIgnoreCaseOrTitleContainingIgnoreCase(encodedString, encodedString);
    }

}
