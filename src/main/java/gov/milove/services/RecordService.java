package gov.milove.services;

import gov.milove.domain.digitalQueue.Record;
import gov.milove.domain.digitalQueue.RecordDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface RecordService {

    Record save(Record record);

    List<LocalTime> findAllServiceIdAndDate(Long serviceId,LocalDate date);

    List<List<RecordDto>> findWeek(Long serviceId, LocalDate date);

    Optional<Record> findById(Long id);
}
