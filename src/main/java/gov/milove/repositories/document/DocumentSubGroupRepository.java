package gov.milove.repositories.document;

import gov.milove.domain.SubGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DocumentSubGroupRepository extends JpaRepository<SubGroup, Long> {

    @Query(value = "select * from sub_group where sub_group.document_group_id = :id order by date_of_creation desc", nativeQuery = true)
    List<SubGroup> findAllByDocumentGroupId(@Param("id") Long id);

    @Query("select u.title from SubGroup u where u.id =?1")
    String getTitleById(@Param("id") Long sub_group_id);



    @Transactional
    @Modifying
    @Query("update SubGroup s set s.title = :newTitle where s.id = :id")
    void editTitle(@Param("newTitle") String newTitle, @Param("id") Long id);
}
