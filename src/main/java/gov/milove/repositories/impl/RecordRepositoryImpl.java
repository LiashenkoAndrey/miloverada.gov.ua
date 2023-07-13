package gov.milove.repositories.impl;

import gov.milove.domain.digitalQueue.Record;
import gov.milove.domain.digitalQueue.RecordDto;
import gov.milove.repositories.CustomRecordRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecordRepositoryImpl implements CustomRecordRepo {
    private static final Logger logger = LoggerFactory.getLogger(RecordRepositoryImpl.class);

    @PersistenceContext
    private EntityManager manager;


    @Override
    @Transactional
    public List<List<RecordDto>> findWeek(Long serviceId, LocalDate date) {

        DayOfWeek d = date.getDayOfWeek();
        LocalDate newDate = date.minusDays(d.ordinal());
        List<List<RecordDto>> listsOfRecords = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            List<RecordDto> records = manager.createQuery("""
                    select new gov.milove.domain.digitalQueue.RecordDto(r.dateOfVisit, r.timeOfVisit, r.person) 
                    from Record r 
                    where r.dateOfVisit = :d 
                    and r.service.id = :service_id
                    order by r.timeOfVisit
                    """, RecordDto.class)
                    .setParameter("d", newDate)
                    .setParameter("service_id", serviceId)
                    .getResultList();
            newDate = newDate.plusDays(1);
            listsOfRecords.add(records);
        }

        return listsOfRecords;
    }

}
