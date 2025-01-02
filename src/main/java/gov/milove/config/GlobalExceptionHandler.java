package gov.milove.config;

import com.mongodb.MongoTimeoutException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

    @ExceptionHandler(MongoTimeoutException.class)
    public void handleNotFound(final HttpServletRequest request, final Exception error) {
        log.error(error.getMessage());
    }
}
