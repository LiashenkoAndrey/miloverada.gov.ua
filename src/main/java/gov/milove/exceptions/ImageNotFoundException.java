package gov.milove.exceptions;

public class ImageNotFoundException extends RuntimeException {


    public ImageNotFoundException() {
    }

    public ImageNotFoundException(String message) {
        super(message);
    }

    public ImageNotFoundException(Throwable cause) {
        super(cause);
    }
}
