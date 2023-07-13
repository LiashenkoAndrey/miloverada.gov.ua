package gov.milove.services.impl;

import gov.milove.domain.digitalQueue.Record;
import gov.milove.domain.digitalQueue.RecordDto;
import gov.milove.repositories.RecordRepository;
import gov.milove.services.RecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService  {

    private final RecordRepository repository;

    @Override
    public Record save(Record record) {
        return repository.save(record);
    }

    @Override
    public List<LocalTime> findAllServiceIdAndDate(Long serviceId, LocalDate date) {
        return repository.findAllServiceIdAndDate(serviceId, date);
    }

    @Override
    public List<List<RecordDto>> findWeek(Long serviceId, LocalDate date) {
        return repository.findWeek(serviceId, date);
    }

    @Override
    public Optional<Record> findById(Long id) {
        return repository.findById(id);
    }

}
