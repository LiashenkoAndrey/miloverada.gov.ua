package gov.milove.services.document;

import gov.milove.domain.Document;
import gov.milove.domain.DocumentGroup;
import gov.milove.repositories.impl.DocumentRepositoryMongo;
import gov.milove.repositories.document.DocumentGroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentGroupService {

    private final DocumentGroupRepository subGroupRepository;

    private final DocumentRepositoryMongo documentRepositoryMongo;

    private final DocumentGroupRepository documentGroupRepository;

    public DocumentGroupService(DocumentGroupRepository subGroupRepository,
                                DocumentRepositoryMongo documentRepositoryMongo,
                                DocumentGroupRepository documentGroupRepository) {
        this.subGroupRepository = subGroupRepository;
        this.documentRepositoryMongo = documentRepositoryMongo;
        this.documentGroupRepository = documentGroupRepository;
    }

    public void createSubGroup(Long group_id,String title) {
        DocumentGroup documentGroup = documentGroupRepository.
                findById(group_id).orElseThrow(EntityNotFoundException::new);

//        subGroupRepository.save(new SubGroup(title, documentGroup, new Date()));
    }

    public boolean deleteSubGroup(Long subGroupId) {
        boolean status;
        try {
            DocumentGroup documentGroup = subGroupRepository.findById(subGroupId)
                    .orElseThrow(EntityNotFoundException::new);
            List<Document> documentList = documentGroup.getDocuments();
            List<String> documentsId = new ArrayList<>(documentList.size());
            for (Document d : documentList) {
                documentsId.add(d.getName());
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

    public List<DocumentGroup> findAllByGroupId(Long id) {
        return subGroupRepository.findAllByDocumentGroupId(id);
    }


//    public String getTitleById(Long sub_group_id) {
//        return subGroupRepository.getTitleById(sub_group_id);
//    }
}
