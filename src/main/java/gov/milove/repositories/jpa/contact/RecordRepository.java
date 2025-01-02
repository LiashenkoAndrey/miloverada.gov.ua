package gov.milove.repositories.jpa.contact;

import gov.milove.domain.digitalqueue.Record;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Repository
public interface RecordRepository extends JpaRepository<Record, Long> {


}
