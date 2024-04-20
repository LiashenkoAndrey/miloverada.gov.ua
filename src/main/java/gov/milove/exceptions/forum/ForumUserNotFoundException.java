package gov.milove.exceptions.forum;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ForumUserNotFoundException extends RuntimeException {


    public ForumUserNotFoundException() {
    }

    public ForumUserNotFoundException(String message) {
        super(message);
    }

    public ForumUserNotFoundException(Throwable cause) {
        super(cause);
    }
}
