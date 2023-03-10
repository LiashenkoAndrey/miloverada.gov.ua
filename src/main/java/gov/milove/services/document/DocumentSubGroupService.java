package gov.milove.services.document;

import gov.milove.domain.CustomDate;
import gov.milove.domain.Document;
import gov.milove.domain.DocumentGroup;
import gov.milove.domain.SubGroup;
import gov.milove.repositories.document.DocumentGroupRepository;
import gov.milove.repositories.implementation.DocumentRepositoryMongo;
import gov.milove.repositories.document.DocumentSubGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentSubGroupService {

    private final DocumentSubGroupRepository subGroupRepository;

    private final DocumentRepositoryMongo documentRepositoryMongo;

    private final DocumentGroupRepository documentGroupRepository;

    public DocumentSubGroupService(DocumentSubGroupRepository subGroupRepository,
                                   DocumentRepositoryMongo documentRepositoryMongo,
                                   DocumentGroupRepository documentGroupRepository) {
        this.subGroupRepository = subGroupRepository;
        this.documentRepositoryMongo = documentRepositoryMongo;
        this.documentGroupRepository = documentGroupRepository;
    }

    public void createSubGroup(Long group_id,String title) {
        DocumentGroup documentGroup = documentGroupRepository.
                findById(group_id).orElseThrow(EntityNotFoundException::new);

        SubGroup newSubGroup = new SubGroup();
        newSubGroup.setTitle(title);
        newSubGroup.setDocument_group(documentGroup);
        newSubGroup.setDate_of_creation(new CustomDate());
        subGroupRepository.save(newSubGroup);
    }

    public boolean deleteSubGroup(Long subGroupId) {
        boolean status;
        try {
            SubGroup subGroup = subGroupRepository.findById(subGroupId)
                    .orElseThrow(EntityNotFoundException::new);
            List<Document> documentList = subGroup.getDocuments();
            List<String> documentsId = new ArrayList<>(documentList.size());
            for (Document d : documentList) {
                documentsId.add(d.getDocument_filename());
            }
            documentRepositoryMongo.deleteDocumentsById(documentsId);
            subGroupRepository.deleteById(subGroupId);
            status = true;
        } catch (Exception ex) {
            status = false;
            ex.printStackTrace();
        }
        return status;
    }

    public List<SubGroup> findAllByGroupId(Long id) {
        return subGroupRepository.findAllByDocumentGroupId(id);
    }


    public String getTitleById(Long sub_group_id) {
        return subGroupRepository.getTitleById(sub_group_id);
    }
}
