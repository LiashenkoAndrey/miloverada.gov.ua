package gov.milove.services;

import gov.milove.domain.institution.Institution;
import gov.milove.repositories.InstitutionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public Optional<Institution> findInstitutionByTitle(String title) {
        return institutionRepository.findByTitle(title);
    }

    public Optional<Institution> findInstitutionById(Long id) {
        return institutionRepository.findById(id);
    }

}
