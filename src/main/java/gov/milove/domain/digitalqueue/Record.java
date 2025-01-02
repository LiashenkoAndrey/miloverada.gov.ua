package gov.milove.domain.digitalqueue;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Immutable
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "queue_records", schema = "queue")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate dateOfVisit;

    @NotNull
    private LocalTime timeOfVisit;


    @OneToOne(cascade = CascadeType.ALL)
    private Person person;

    @OneToOne(cascade = CascadeType.ALL)
    private Service service;

}
