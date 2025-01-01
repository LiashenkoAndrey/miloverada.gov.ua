package gov.milove.services.institution;


import gov.milove.domain.institution.Deputy;
import gov.milove.exceptions.DeputyServiceException;
import gov.milove.repositories.jpa.institution.DeputyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeputyService {

    private final DeputyRepository repository;

    public DeputyService(DeputyRepository repository) {
        this.repository = repository;
    }

    public void save(Deputy deputy) throws DeputyServiceException {
        try {
            repository.save(deputy);
        } catch (Exception ex) {
            throw new DeputyServiceException (ex.getMessage());
        }
    }


    public void deleteById(Long deputy_id) throws DeputyServiceException {
        try {
            repository.deleteById(deputy_id);
        } catch (Exception ex) {
            throw new DeputyServiceException(ex.getMessage());
        }
    }

    public Optional<Deputy> findById(Long id) throws DeputyServiceException {
        try {
            return repository.findById(id);
        } catch (Exception ex) {
            throw new DeputyServiceException(ex.getMessage());
        }
    }

    public List<Deputy> findAll() {
        return repository.findAll();
    }
}
