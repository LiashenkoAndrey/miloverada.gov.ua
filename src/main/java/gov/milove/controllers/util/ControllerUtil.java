package gov.milove.controllers.util;

import gov.milove.domain.News;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ControllerUtil {

    public static ResponseEntity<String> ok(String message) {
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public static ResponseEntity<String> error(String message) {
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }



}
