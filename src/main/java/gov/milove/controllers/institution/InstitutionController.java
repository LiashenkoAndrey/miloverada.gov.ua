package gov.milove.controllers.institution;

import gov.milove.domain.dto.InstitutionDto;
import gov.milove.domain.institution.Institution;
import gov.milove.exceptions.InstitutionNotFoundException;
import gov.milove.repositories.jpa.institution.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InstitutionController {


    private final InstitutionRepository repository;

    @GetMapping("/institution/all")
    private List<InstitutionDto> getAll() {
        return repository.getAllAsDto();
    }

    @GetMapping("/institution/{id}")
    private Institution getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(InstitutionNotFoundException::new);
    }


}
