package gov.milove.services.institution;

import gov.milove.domain.institution.InstitutionEmployee;
import gov.milove.exceptions.EmployeeServiceException;
import gov.milove.repositories.jpa.institution.InstitutionEmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class InstitutionEmployeeService {

    private final InstitutionEmployeeRepository institutionEmployeeRepository;

    public InstitutionEmployeeService(InstitutionEmployeeRepository institutionEmployeeRepository) {
        this.institutionEmployeeRepository = institutionEmployeeRepository;
    }

    public InstitutionEmployee save(InstitutionEmployee employee) throws EmployeeServiceException {
        try {
            return institutionEmployeeRepository.save(employee);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new EmployeeServiceException("Error when saving");
        }
    }

//    public Optional<InstitutionEmployee> findById(Long id) {
//        return institutionEmployeeRepository.findById(id);
//    }

    public void deleteById(Long id) throws EmployeeServiceException {
        try {
            institutionEmployeeRepository.deleteById(id);
        } catch (Exception ex) {
            throw new EmployeeServiceException("Employee with id:" + id + ", does not exists.");
        }
    }

//    public List<InstitutionEmployee> findAllByInstitutionTitle(String title) {
//        return institutionEmployeeRepository.findByInstitution_Title(title);
//    }
}
