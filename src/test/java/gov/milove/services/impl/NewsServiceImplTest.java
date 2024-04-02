package gov.milove.services.impl;

import gov.milove.domain.News;
import gov.milove.domain.NewsImage;
import gov.milove.repositories.NewsImageRepo;
import gov.milove.repositories.NewsRepository;
import gov.milove.services.NewsImagesService;
import gov.milove.services.NewsImagesServiceImpl;
import gov.milove.services.NewsService;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@Log4j2
class NewsServiceImplTest {

    @InjectMocks
    private NewsServiceImpl service;

    @MockBean
    private NewsImagesServiceImpl newsImagesService;

    @MockBean
    private NewsRepository newsRepository;

    @MockBean
    private NewsImageRepo newsImageRepo;

    @Test
    void saveNews() {
        NewsImage newsImage = new NewsImage("filename", ObjectId.get().toHexString());
        when(newsImagesService.saveAll(any())).thenReturn(List.of(newsImage));

        News news = service.save(new News(), new MultipartFile[]{}, LocalDateTime.now());
        assertEquals(1, news.getImages().size());
        assertEquals(newsImage, news.getImages().get(0));
    }

    @Test
    void deleteNewsImageById() {
        String id =ObjectId.get().toHexString();
        service.deleteNewsImageById(id);
        verify(newsImagesService).deleteFromMongoIfNotUsed(id);
        verify(newsImageRepo).deleteByMongoImageId(id);
    }
  
}