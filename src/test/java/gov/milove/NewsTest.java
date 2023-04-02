package gov.milove;

import gov.milove.domain.CustomDate;
import gov.milove.domain.News;
import gov.milove.repositories.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsTest {

    @Autowired
    private NewsRepository newsRepo;

    @Test
    public void creatingNews() {
//        News news = newsRepo.findById(81L).orElseThrow(EntityNotFoundException::new);
//        System.out.println(LocalDate.of(2003,12,20) + "" + news.getCreated().toLocalDate());
        //        System.out.println(news.getDate_of_creation().toLocalDate());
//        System.out.println(news.getDate_of_creation().toLocalTime());
//        System.out.println(news.getDate_of_creation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

    }
}
