package gov.milove.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerUtil {

    static ResponseEntity<String> ok(String message) {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    static ResponseEntity<String> error(String message) {
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
