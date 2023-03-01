package gov.milove.repositories;


import gov.milove.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("from Document d where d.sub_group =?1")
    List<Document> findAllBySub_group(Long id);

    @Transactional
    @Modifying
    @Query(value = "delete from document where document_filename =:doc_id", nativeQuery = true)
    void deleteByDocument_id(@Param("doc_id") String id);
}
