package gov.milove.services.administration;

import gov.milove.exceptions.AdministrationGroupServiceException;
import gov.milove.repositories.jpa.institution.AdministrationGroup;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministrationGroupService {



    private final AdministrationGroup repository;
    private final AdministrationEmployeeService employeeService;


    public List<gov.milove.domain.administration.AdministrationGroup> findAllGroups() {
        return repository.findAllWhereGroupIdIsNull();
    }

    public void save(gov.milove.domain.administration.AdministrationGroup newGroup) throws AdministrationGroupServiceException {
        try {
            repository.save(newGroup);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new AdministrationGroupServiceException(ex.getMessage());
        }
    }


    public Optional<gov.milove.domain.administration.AdministrationGroup> findById(Long id) {
        return repository.findById(id);
    }

    public void deleteById(Long group_id) throws AdministrationGroupServiceException {
        try {
            gov.milove.domain.administration.AdministrationGroup group = repository.findById(group_id).orElseThrow(EntityNotFoundException::new);

            // delete dependencies
            if (!group.getGroup_list().isEmpty()) {
                for (gov.milove.domain.administration.AdministrationGroup g : group.getGroup_list()) {
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
    private void clearAndDelete(gov.milove.domain.administration.AdministrationGroup group) throws AdministrationGroupServiceException {
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
