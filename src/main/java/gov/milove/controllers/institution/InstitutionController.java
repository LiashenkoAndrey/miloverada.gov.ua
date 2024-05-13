package gov.milove.controllers.institution;

import gov.milove.domain.dto.InstitutionDto;
import gov.milove.domain.institution.Institution;
import gov.milove.exceptions.InstitutionNotFoundException;
import gov.milove.repositories.institution.InstitutionRepository;
import gov.milove.services.institution.InstitutionEmployeeService;
import gov.milove.services.institution.InstitutionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InstitutionController {


    private final InstitutionRepository inst_repo;

    @GetMapping("/institution/all")
    private List<InstitutionDto> getAll() {
        return inst_repo.getAllAsDto();
    }

    @GetMapping("/institution/{id}")
    private Institution getById(@PathVariable Long id) {
        return inst_repo.findById(id).orElseThrow(InstitutionNotFoundException::new);
    }


}
