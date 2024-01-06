package gov.milove.util;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;

@Value
@Getter
@Setter
public class ErrorMessage {

    String message;

    public static ErrorMessage from(final String message) {
        return new ErrorMessage(message);
    }
}