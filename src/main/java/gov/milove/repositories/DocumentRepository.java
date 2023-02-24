package gov.milove.repositories;


import gov.milove.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    @Query("from Document d where d.sub_group =?1")
    List<Document> findAllBySub_group(Long id);
}
