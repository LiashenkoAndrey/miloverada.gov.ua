package gov.milove.services.administration;

import gov.milove.domain.administration.AdministrationGroup;
import gov.milove.domain.dto.AdministrationGroupDto;
import gov.milove.exceptions.AdministrationGroupServiceException;
import gov.milove.repositories.administration.AdministrationGroupRepository;
import gov.milove.services.document.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdministrationGroupService {

    public AdministrationGroupService(AdministrationGroupRepository repository, AdministrationEmployeeService employeeService, @Qualifier("administrationDocumentService") DocumentService documentService) {
        this.repository = repository;
        this.employeeService = employeeService;
        this.documentService = documentService;
    }

    private final AdministrationGroupRepository repository;
    private final AdministrationEmployeeService employeeService;

    private final DocumentService documentService;

    public List<AdministrationGroup> findAllGroups() {
        return repository.findAllWhereGroupIdIsNull();
    }

    public void save(AdministrationGroup newGroup) throws AdministrationGroupServiceException {
        try {
            repository.save(newGroup);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AdministrationGroupServiceException(ex.getMessage());
        }
    }

    public Optional<AdministrationGroupDto> findDtoById(Long id) {
        return repository.findDtoById(id);
    }

    public Optional<AdministrationGroup> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long group_id) throws AdministrationGroupServiceException {
        try {
            AdministrationGroup group = repository.findById(group_id).orElseThrow(EntityNotFoundException::new);

            // delete dependencies
            if (!group.getGroup_list().isEmpty()) {
                for (AdministrationGroup g : group.getGroup_list()) {
                    clearAndDelete(g);
                }
            }
            // delete group
            clearAndDelete(group);
            repository.deleteGroupById(group_id);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AdministrationGroupServiceException(ex.getMessage());
        }
    }


    /**
     * deletes all dependencies of the group and delete the group itself
     * @param group group for deleting
     * @throws AdministrationGroupServiceException the exception to the service
     */
    private void clearAndDelete(AdministrationGroup group) throws AdministrationGroupServiceException {
        try {
            employeeService.deleteAll(group.getEmployee_list());
//            documentService.deleteAll(group.getDocument_list());
            repository.deleteById(group.getId());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AdministrationGroupServiceException(ex.getMessage());
        }
    }

}
