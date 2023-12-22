package gov.milove.repositories.impl;

import gov.milove.domain.dto.DocumentGroupDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentGroupRepoImpl implements CustomDocumentGroupRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DocumentGroupDto> findAllDto() {
        String s = "select * from sub_group where document_group_id = 3;";

        List<DocumentGroupDto> res = entityManager.createNativeQuery("select dg.id, dg.title from document_group dg", DocumentGroupDto.class)
                .setMaxResults(1)
                .getResultList();
        return res;
    }
}
