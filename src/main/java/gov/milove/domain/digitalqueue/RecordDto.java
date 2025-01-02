package gov.milove.domain.digitalqueue;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NotNull
@AllArgsConstructor
public class RecordDto {

    private LocalDate dateOfVisit;

    private LocalTime timeOfVisit;

    private Person person;
}
