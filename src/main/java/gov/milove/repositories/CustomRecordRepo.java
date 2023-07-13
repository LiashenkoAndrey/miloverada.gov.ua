package gov.milove.repositories;

import gov.milove.domain.digitalQueue.RecordDto;

import java.time.LocalDate;
import java.util.List;

public interface CustomRecordRepo {

    List<List<RecordDto>> findWeek(Long serviceId, LocalDate date);

}
