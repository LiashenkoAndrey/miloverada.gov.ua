package gov.milove.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NewsImageNotFoundException extends RuntimeException {

    public NewsImageNotFoundException(String message) {
        super(message);
    }


    public NewsImageNotFoundException(Throwable cause) {
        super(cause);
    }
}
