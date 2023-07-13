package gov.milove.repositories;

import gov.milove.domain.digitalQueue.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@org.springframework.stereotype.Repository
public interface RecordRepository extends JpaRepository<Record, Long>, CustomRecordRepo {

    @Query("select r.timeOfVisit from Record r where r.dateOfVisit = :date and r.service.id = :id")
    List<LocalTime> findAllServiceIdAndDate(@Param("id") Long id, @Param("date") LocalDate date);

}
