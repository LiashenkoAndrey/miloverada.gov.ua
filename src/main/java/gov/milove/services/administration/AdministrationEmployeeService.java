package gov.milove.services.administration;


import gov.milove.domain.administration.AdministrationEmployee;
import gov.milove.exceptions.AdministrationEmployeeServiceException;
import gov.milove.repositories.jpa.institution.AdministrationEmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministrationEmployeeService {


    private final AdministrationEmployeeRepository repository;

    public List<AdministrationEmployee> findAllWhereGroupIdIsNull() {
        return repository.findAllWhereGroupIdIsNull();
    }

    public void save(AdministrationEmployee employee) throws AdministrationEmployeeServiceException  {
        try {
            repository.save(employee);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AdministrationEmployeeServiceException(ex.getMessage());
        }
    }

    public Optional<AdministrationEmployee> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long id) throws AdministrationEmployeeServiceException {
        try {
            AdministrationEmployee employee = repository.findById(id).orElseThrow(EntityNotFoundException::new);
            if (employee.getImage_id() != null) {
//                imageService.deleteImageById(employee.getImage_id());
            }
            repository.deleteById(id);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AdministrationEmployeeServiceException(ex.getMessage());
        }
    }

    public void deleteAll(List<AdministrationEmployee> employees) throws AdministrationEmployeeServiceException {
        try {
            for (AdministrationEmployee e : employees) {
                deleteById(e.getId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AdministrationEmployeeServiceException(ex.getMessage());
        }
    }
}
