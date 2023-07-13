package gov.milove.controllers.digitalQueue;

import gov.milove.domain.digitalQueue.Person;
import gov.milove.domain.digitalQueue.Record;
import gov.milove.domain.digitalQueue.RecordDto;
import gov.milove.domain.digitalQueue.Service;
import gov.milove.exceptions.ControllerException;
import gov.milove.repositories.ServiceRepository;
import gov.milove.services.RecordService;
import gov.milove.services.TerritorialCommunityServiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/queue/record")
public class RecordController {

    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    private final ServiceRepository serviceRepository;
    private final RecordService service;

    @PostMapping("/new")
    public ResponseEntity<Long> newRecord(@ModelAttribute Person person,
                            @RequestParam("dateOfVisit") String date,
                            @RequestParam("timeOfVisit") String time,
                            @RequestParam("serviceId") Long serviceId) {

        Service service1 = serviceRepository.findById(serviceId).orElseThrow(EntityNotFoundException::new);

        Record record = service.save(Record.builder()
                .dateOfVisit(LocalDate.parse(date))
                .timeOfVisit(LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm")))
                .person(person)
                .service(service1)
                .build());

        return ResponseEntity.ok(record.getId());
    }

    @GetMapping("/details")
    public String getDetails(@RequestParam("recordId") Long id, Model model) {
        model.addAttribute("record", service.findById(id).orElseThrow(EntityNotFoundException::new));
        return "digitalQueue/recordDetails";
    }

    @GetMapping("/new")
    public String newRecord() {
        return "digitalQueue/newRecord";
    }

    @GetMapping("/find")
    public ResponseEntity<List<LocalTime>> getAllByDate(@RequestParam("date") String date,
                                                        @RequestParam("serviceId") Long serviceId) {
        try {
            logger.info(date + ", " + serviceId);
            List<LocalTime> records = service.findAllServiceIdAndDate(serviceId, LocalDate.parse(date));
            logger.info(records.toString());
            return ResponseEntity.ok(records);

        } catch (Exception ex) {
            logger.info(ex.toString());
            throw new ControllerException(ex);
        }
    }

    @GetMapping("/find-week")
    public ResponseEntity<List<List<RecordDto>>> findWeek(@RequestParam("date") String date,
                                                          @RequestParam("serviceId") Long serviceId) {
        try {
            List<List<RecordDto>> records = service.findWeek(serviceId, LocalDate.parse(date));

            HttpHeaders headers = new HttpHeaders();
            headers.set("Access-Control-Allow-Origin", "*");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(records);

        } catch (Exception ex) {
            logger.info(ex.toString());
            throw new ControllerException(ex);
        }
    }


}
