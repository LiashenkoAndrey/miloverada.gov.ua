package gov.milove.controllers.documents;

import gov.milove.domain.document.Document;
import gov.milove.domain.document.DocumentGroup;
import gov.milove.domain.dto.document.DocumentGroupWithGroupsDto;
import gov.milove.domain.dto.document.DocumentGroupWithGroupsAndDocumentsDto;
import gov.milove.exceptions.DocumentGroupNotFoundException;
import gov.milove.repositories.jpa.document.DocumentGroupRepo;
import gov.milove.services.document.DocumentGroupService;
import gov.milove.services.document.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api")
@Validated
public class DocumentGroupController {

    private final DocumentGroupRepo documentGroupRepo;
    private final DocumentGroupService documentGroupService;
    private final DocumentService documentService;


    @GetMapping("/documentGroup/all")
    public List<DocumentGroupWithGroupsDto> findAll() {
        return documentGroupRepo.findDistinctByDocumentGroupIdOrderByCreatedOn(null);
    }

    @PostMapping("/protected/documentGroup/new")
    public DocumentGroupWithGroupsAndDocumentsDto createNewSubGroup(@RequestParam(required = false) Long groupId,
                                                                    @NotBlank @RequestParam String name) {

        DocumentGroup documentGroup = DocumentGroup.builder()
                .documentGroup(groupId == null ? null : documentGroupRepo.getReferenceById(groupId))
                .name(name)
                .build();
        DocumentGroup saved = documentGroupRepo.save(documentGroup);
        return documentGroupRepo.findDistinctById(saved.getId()).orElseThrow(EntityNotFoundException::new);
    }

    @PutMapping("/protected/documentGroup/{id}/update")
    public Long editSubGroup(@PathVariable Long id,
                             @NotBlank @RequestParam String name) {
        DocumentGroup group = documentGroupRepo.findById(id).orElseThrow(EntityNotFoundException::new);
        group.setName(name);
        documentGroupRepo.save(group);
        return group.getId();
    }

    @DeleteMapping("/protected/documentGroup/{id}/delete")
    public Long deleteSubGroup(@PathVariable Long id) {
        log.info("delete = {}",id );
        documentGroupService.deleteById(id);
        return id;
    }

    @PostMapping("/protected/documentGroup/{id}/document/new")
    public Document newDoc(@PathVariable Long id,
                           @RequestParam MultipartFile file,
                           @RequestParam String title) {
        log.info("new doc = {}, size - {}, title = {}", file.getOriginalFilename(), file.getSize(), title);
        return documentService.saveDocument(id, file, title);
    }

    @GetMapping("/documentGroup/id/{id}")
    public DocumentGroupWithGroupsAndDocumentsDto findById(@PathVariable Long id) {
        return documentGroupRepo.findDistinctById(id)
                .orElseThrow(DocumentGroupNotFoundException::new);
    }
}
