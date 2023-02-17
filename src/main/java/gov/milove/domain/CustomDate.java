package gov.milove.domain;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Embeddable
public class CustomDate {

    private LocalDateTime date_of_creation = LocalDateTime.now();

    public String returnDate() {
        return date_of_creation.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String returnTime() {
        return date_of_creation.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    public LocalDateTime date_of_creation() {
        return date_of_creation;
    }
}
