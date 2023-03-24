package gov.milove.controllers.institution;

import gov.milove.domain.DocumentGroup;
import gov.milove.domain.institution.Institution;
import gov.milove.domain.institution.InstitutionEmployee;
import gov.milove.repositories.institution.InstitutionRepository;
import gov.milove.services.institution.InstitutionEmployeeService;
import gov.milove.services.institution.InstitutionService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/institution/{title}")
public class InstitutionController {

    private final InstitutionService institutionService;
    private final InstitutionEmployeeService institutionEmployeeService;

    private final InstitutionRepository inst_repo;

    public InstitutionController(InstitutionService institutionService, InstitutionEmployeeService institutionEmployeeService, InstitutionRepository inst_repo) {
        this.institutionService = institutionService;
        this.institutionEmployeeService = institutionEmployeeService;
        this.inst_repo = inst_repo;
    }

    @GetMapping
    public String getInstitution(@PathVariable("title") String title,
                                 Model model) {
        Optional<Institution> institution = institutionService.findInstitutionByTitle(title);
        if (institution.isPresent()) {
            DocumentGroup group = institution.get().getDocument_group();
            model.addAttribute("group", group);
            model.addAttribute("institution", institution.get());
            model.addAttribute("employee_list", institutionEmployeeService.findAllByInstitutionTitle(title));
            model.addAttribute("employee", new InstitutionEmployee());
            return "institution";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> update(@PathVariable("title") Long id, @RequestParam("title") String new_title) {
        try {
            Institution institution = inst_repo.findById(id)
                    .orElseThrow(EntityNotFoundException::new);
            inst_repo.updateTitleById(new_title, institution.getId());
            return new ResponseEntity<>("Оновлення успішне", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>("Виникли проблеми з оновленням", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
