package gov.milove;

import gov.milove.repositories.NewsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsServiceImplTest {

    @Autowired
    private NewsRepository newsRepo;

    @Test
    public void update() {
    }

    public void save() {

    }

    public void delete() {

    }


}
