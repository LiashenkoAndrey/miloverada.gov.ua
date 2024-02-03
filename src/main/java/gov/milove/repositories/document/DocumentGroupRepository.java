package gov.milove.repositories.document;

import gov.milove.domain.DocumentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentGroupRepository extends JpaRepository<DocumentGroup, Long> {

    @Query(value = "select * from document_group where document_group.document_group_id = :id order by created_on desc", nativeQuery = true)
    List<DocumentGroup> findAllByDocumentGroupId(@Param("id") Long id);

//    @Query("select u.title from DocumentGroup u where u.id =?1")
//    String getTitleById(@Param("id") Long sub_group_id);



//    @Transactional
//    @Modifying
//    @Query("update DocumentGroup s set s.title = :newTitle where s.id = :id")
//    void editTitle(@Param("newTitle") String newTitle, @Param("id") Long id);
}
