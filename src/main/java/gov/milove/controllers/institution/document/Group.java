package gov.milove.controllers.institution.document;

import gov.milove.domain.DocumentGroup;
import gov.milove.domain.dto.DocumentDtoResultTransformer;
import gov.milove.domain.dto.DocumentGroupDto;
import gov.milove.repositories.document.DocumentGroupRepository;
import gov.milove.services.document.DocumentGroupService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.query.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Log4j2
@RestController
@RequestMapping("/api/documentGroup")
@RequiredArgsConstructor
public class Group {

    private final DocumentGroupService documentGroupService;

    private final DocumentGroupRepository doc_group_repo;

    @GetMapping("/view/{groupId}")
    public String group(@PathVariable("groupId") Long group_id,
                        Model model) {
        Optional<DocumentGroup> documentGroup = documentGroupService.findDocumentGroupById(group_id);
        if (documentGroup.isPresent()) {

            model.addAttribute("group", documentGroup.get());
            model.addAttribute("groups",documentGroupService.findAll());
            return "group";
        } else {
            return "error/404";
        }
    }

    @PersistenceContext
    private EntityManager em;

    @GetMapping("/all")
    public Collection getAll() {
        DocumentDtoResultTransformer transformer = new DocumentDtoResultTransformer();

        List<DocumentGroupDto> documentGroupDtos = em.createQuery("""
        select sub_group.id as s_id,
               sub_group.title as s_title,
               d.id as d_id,
               d.title as d_title
        from DocumentGroup d 
        join d.subGroups sub_group order by d.id
        """)
                .unwrap(Query.class)
                .setResultTransformer(transformer)
                .getResultList();
        return transformer.getDocumentGroupDtoMap().values();
    }



    @PostMapping("/new")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createNewGroup(@RequestParam("groupTitle") String title) {
        documentGroupService.createGroup(title);
        return "redirect:/";
    }

    @PostMapping("/{groupId}/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> edit(@RequestParam("title") String title, @PathVariable("groupId") Long id) {
        doc_group_repo.updateTitle(title, id);
        return ok("Оновлення успішне");
    }


    @GetMapping("/delete")
    public ResponseEntity<String> deleteGroup (@RequestParam("groupId") Long id) {
        boolean success = documentGroupService.deleteGroup(id);
        if (success) return ok("Видалення успішне");
        else return error("Виникли проблеми з видаленням");
    }
}
