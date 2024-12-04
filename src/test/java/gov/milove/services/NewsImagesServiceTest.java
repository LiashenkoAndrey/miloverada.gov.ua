package gov.milove.services;

import gov.milove.repositories.jpa.NewsImageRepo;
import gov.milove.repositories.mongo.NewsImagesMongoRepo;
import gov.milove.services.impl.NewsImagesServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@Log4j2
class NewsImagesServiceTest {

    @InjectMocks
    private NewsImagesServiceImpl newsImagesService;

    @MockBean
    private NewsImageRepo newsImageRepo;

    @MockBean
    private NewsImagesMongoRepo newsImagesMongoRepo;

    @Test
    void saveAll() {
    }

    @Test
    void deleteAllIfNotUsed() {
    }

    @Test
    void deleteFromMongoIfNotUsed() {
//        when(newsImageIsUsedMoreThenOneTime)
    }
}
