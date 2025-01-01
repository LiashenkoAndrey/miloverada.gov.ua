package gov.milove.domain.dto.document.media;

import gov.milove.domain.dto.forum.TopicDto;
import gov.milove.domain.forum.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Long> {


    @Query("select t from Topic t")
    List<TopicDto> getList();
}
