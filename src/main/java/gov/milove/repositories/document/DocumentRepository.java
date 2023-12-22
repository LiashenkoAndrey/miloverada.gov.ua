package gov.milove.repositories.document;

import gov.milove.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("select new gov.milove.domain.Document(d.id, d.title, d.document_filename) from Document d where d.sub_group.id = :id")
    List<Document> findAllBySubGroupId(@Param("id") Long id);

    @Query("from Document d where d.document_filename =?1")
    Optional<Document> findByDocument_filename(String filename);

    @Transactional
    @Modifying
    @Query(value = "delete from document where document_filename =:doc_id", nativeQuery = true)
    void deleteByDocument_filename(@Param("doc_id") String id);
}
