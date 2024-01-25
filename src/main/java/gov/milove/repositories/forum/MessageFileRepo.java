package gov.milove.repositories.forum;

import gov.milove.domain.forum.MessageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageFileRepo extends JpaRepository<MessageFile, Long> {

    @Query(value = "select * from forum.message_file mf where mf.file_id = :messageFileId;", nativeQuery = true)
    Boolean isFileUsedMoreThenOneTime(@Param("messageFileId") Long messageFileId);
}
