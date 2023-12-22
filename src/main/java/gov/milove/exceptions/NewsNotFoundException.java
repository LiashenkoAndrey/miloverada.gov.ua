package gov.milove.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NewsNotFoundException extends EntityNotFoundException {

    public NewsNotFoundException() {
    }

    public NewsNotFoundException(String message) {
        super(message);
    }
}
