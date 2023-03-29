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

    public static List<List<News>> packageNews(Page<News> pages, int amountOfNews) {
        List<List<News>> dividedPages = new ArrayList<>();
        Iterator<News> iterator = pages.iterator();
        for (int i = 0; i < amountOfNews/3; i++) {
            List<News> newsList = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (iterator.hasNext()) {
                    newsList.add(iterator.next());
                }
            }
            dividedPages.add(newsList);
        }
        return dividedPages;
    }

}
