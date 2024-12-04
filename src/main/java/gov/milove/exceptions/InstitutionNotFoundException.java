package gov.milove.exceptions;

public class InstitutionNotFoundException extends RuntimeException {

    public InstitutionNotFoundException() {
    }

    public InstitutionNotFoundException(String message) {
        super(message);
    }

    public InstitutionNotFoundException(Throwable cause) {
        super(cause);
    }
}
