package gov.milove.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@NoArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalParameterException extends ControllerException {

    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(Throwable cause) {
        super(cause);
    }
}
