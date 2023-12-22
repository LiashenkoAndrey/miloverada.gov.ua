package gov.milove.repositories;

import gov.milove.domain.forum.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findAllByChat_Id(Long id);

    @Query("from Message m order by m.createdOn desc")
    List<Message> findLatest(Pageable pageable);
}
