package gov.milove.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.milove.domain.News;
import gov.milove.domain.NewsImage;
import gov.milove.domain.NewsType;
import gov.milove.exceptions.NewsImageNotFoundException;
import gov.milove.exceptions.NewsNotFoundException;
import gov.milove.repositories.jpa.NewsRepository;
import gov.milove.repositories.jpa.NewsTypeRepo;
import gov.milove.services.NewsImagesService;
import gov.milove.services.NewsService;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
@Log4j2
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    static MultiValueMap<String, String> params = new LinkedMultiValueMap<>(3);
    static News defaultNews;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void fillParams() {
        params.add("title", "Title!");
        params.add("text", "Main text");
        params.add("dateOfPublication", LocalDateTime.now().toString());

       defaultNews = News.builder()
                .id(5L)
                .description(params.get("title").toString())
                .main_text(params.get("text").toString())
                .dateOfPublication(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    public void before() {
        objectMapper = new ObjectMapper();
    }


    @MockBean
    private NewsService newsService;

    @MockBean
    private NewsTypeRepo newsTypeRepo;

    @MockBean
    private NewsRepository newsRepository;

    @MockBean
    private NewsImagesService newsImagesService;

    @Nested
    class DeleteNewsTest {
        @Test
        void deleteNewsByIdWithCorrectIdAndNewsExists() throws Exception {
            mockMvc.perform(delete("/api/protected/news/5/delete"))
                    .andExpect(status().isAccepted());
            verify(newsService, times(1)).deleteById(any());
        }

        @Test
        void deleteNewsByIdWithCorrectIdAndNewsNotExist() throws Exception {
            doThrow(NewsNotFoundException.class).when(newsService).deleteById(any());
            mockMvc.perform(delete("/api/protected/news/5/delete"))
                    .andExpect(status().isNotFound());
            verify(newsService, times(1)).deleteById(any());
        }

        @Test
        void deleteNewsByIdWithInCorrectId() throws Exception {
            mockMvc.perform(delete("/api/protected/news/-5/delete"))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(delete("/api/protected/news/0/delete"))
                    .andExpect(status().isBadRequest());

            verify(newsService, times(0)).deleteById(any());
        }

    }


    @Test
    void getNewsTypes() throws Exception {
        when(newsTypeRepo.findAll()).thenReturn(List.of(new NewsType("title", "desc")));
        mockMvc.perform(get("/api/protected/news-types"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("$.size()").value(1),
                        jsonPath("$.[0].title").value("title"))
                .andReturn();
    }

    @Nested
    class NewsImageTest {

        @Test
        @DisplayName("delete image where id is correct and image exists")
        void deleteNewsImageByIdWithIdCorrectIdAndImageExists() throws Exception {
            String imgId = ObjectId.get().toHexString();
            String url = String.format("/api/protected/news/image/%s/delete", imgId);
            mockMvc.perform(delete(url))
                    .andExpectAll(
                            status().isAccepted(),
                            jsonPath("$").value(imgId)
                    );
        }

        @Test()
        @DisplayName("delete image where id is correct but image NOT exist")
        void deleteNewsImageByIdWithIdCorrectIdAndImageNotExist() throws Exception {
            String imgId = ObjectId.get().toHexString();
            String url = String.format("/api/protected/news/image/%s/delete", imgId);
            doThrow(NewsImageNotFoundException.class).when(newsService).deleteNewsImageById(any());
            mockMvc.perform(delete(url))
                    .andExpect(status().isNotFound());
            verify(newsService, times(1)).deleteNewsImageById(any());
        }

        @Test
        @DisplayName("delete image where id is NOT correct")
        void deleteNewsImageByIdWithIncorrectId() throws Exception {
            mockMvc.perform(delete("/api/protected/news/image/23424/delete"))
                    .andExpect(status().isBadRequest());

            mockMvc.perform(delete("/api/protected/news/image//delete"))
                    .andExpect(status().isBadRequest());

            verify(newsService, times(0)).deleteNewsImageById(any());
        }

    }



    @Test
    void saveNewsType() throws Exception {
        NewsType newsType = new NewsType("title", "exp");
        String json = objectMapper.writeValueAsString(newsType);
        when(newsTypeRepo.save(any())).thenReturn(newsType);

        MockHttpServletResponse response = mockSaveNewsType(json);

        NewsType saved = objectMapper.readValue(response.getContentAsString(), new TypeReference<>() {});
        assertEquals(newsType, saved);
        assertEquals(200, response.getStatus());
    }

    @Test
    void saveNewsTypeWithEmptyTitle() throws Exception {
        NewsType newsType = new NewsType("", "");
        String json = objectMapper.writeValueAsString(newsType);
        when(newsTypeRepo.save(any())).thenReturn(newsType);

        MockHttpServletResponse response = mockSaveNewsType(json);
        assertEquals(400, response.getStatus());
    }

    @Nested
    class DeleteNewsTypeTest {

        @Test
        @DisplayName("delete with id higher then zero and news type exists")
        void deleteNewsTypeByIdWhereIdHigherThenZero() throws Exception {
            mockMvc.perform(delete("/api/protected/newsType/5/delete"))
                    .andExpect(status().isOk());
            verify(newsTypeRepo, times(1)).deleteById(5L);
        }

        @Test
        @DisplayName("delete by correct id by entity not exist")
        void deleteNewsTypeByIdWhereIdHigherThenZeroButNotExist() throws Exception {
            doThrow(NewsNotFoundException.class).when(newsTypeRepo).deleteById(any());
            mockMvc.perform(delete("/api/protected/newsType/5/delete"))
                    .andExpect(status().isNotFound());
            verify(newsTypeRepo, times(1)).deleteById(any());
        }

        @Test
        @DisplayName("delete by incorrect id")
        void deleteNewsTypeByIncorrectId() throws Exception {
            mockMvc.perform(delete("/api/protected/newsType/-7/delete"))
                            .andExpect(status().isBadRequest());

            mockMvc.perform(delete("/api/protected/newsType/0/delete"))
                    .andExpect(status().isBadRequest());

            verify(newsTypeRepo, times(0)).deleteById(any());
        }
    }


    @Test
    void saveNewsTypeWithNullFields() throws Exception {
        NewsType newsType = new NewsType(null, null);
        String json = objectMapper.writeValueAsString(newsType);
        when(newsTypeRepo.save(any())).thenReturn(newsType);

        MockHttpServletResponse response = mockSaveNewsType(json);
        assertEquals(400, response.getStatus());
    }

    private MockHttpServletResponse mockSaveNewsType(String json) throws Exception {
        return mockMvc.perform(post("/api/protected/newsType/new")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
    }

    @Test
    void newNewsWithoutNewsType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "Hello, World!".getBytes());

        when(newsService.save(any(), any(), any())).thenReturn(defaultNews);

        MockHttpServletResponse response = mockMvc.perform(multipart("/api/protected/news/new")
                        .file(file)
                        .params(params)
                        .param("newsTypeId", "-1"))
                .andReturn().getResponse();
        assertEquals("5", response.getContentAsString());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        verify(newsTypeRepo, times(0)).getReferenceById(any());
    }

    @Test
    void newNewsWithNewsType() throws Exception {
        MockMultipartFile file = new MockMultipartFile("images", "Hello, World!".getBytes());
        Long newsTypeId = 4L;
        NewsType newsType = new NewsType(newsTypeId,"title", "exp");

        when(newsService.save(any(), any(), any())).thenReturn(defaultNews);
        when(newsTypeRepo.getReferenceById(newsTypeId)).thenReturn(newsType);

        MockHttpServletResponse response = mockMvc.perform(multipart("/api/protected/news/new")
                        .file(file)
                        .params(params)
                        .param("newsTypeId", newsTypeId.toString()))
                .andReturn().getResponse();

        assertEquals("5", response.getContentAsString());
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        verify(newsTypeRepo, times(1)).getReferenceById(newsTypeId);
    }

    @Nested
    class UpdateNewsTest {

        @Test
        void updateExistingNews() throws Exception {
            when(newsRepository.findById(any())).thenReturn(Optional.ofNullable(defaultNews));
            mockMvc.perform(put("/api/protected/news/5/update")
                            .params(params)
                    ).andExpect(status().isAccepted())
                    .andExpect(jsonPath("$").value("5"));
            verify(newsRepository, times(1)).save(any());
        }

        @Test
        void updateNewsButItNotExist() throws Exception {
            when(newsRepository.findById(5L)).thenReturn(Optional.empty());
            mockMvc.perform(put("/protected/news/5/update")
                            .params(params)
                    ).andExpect(status().isNotFound());
            verify(newsRepository, times(0)).save(any());
        }
    }

    @Test
    void saveNewNewsImage() throws Exception {
        NewsImage newsImage = new NewsImage("fileName", ObjectId.get().toHexString());
        when(newsRepository.findById(any())).thenReturn(Optional.of(new News()));
        when(newsImagesService.saveAll(any())).thenReturn(List.of(newsImage));
        MockMultipartFile file = new MockMultipartFile("images", "Hello, World!".getBytes());
        mockMvc.perform(multipart("/api/protected/news/5/image/new")
                .file(file)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$.[0].fileName").value(newsImage.getFileName()));
    }

    @Test
    void getSimilarNewsByNewsIdWhereNewsTypeIsNull() throws Exception {
        News news = new News(null);
        when(newsRepository.findById(any())).thenReturn(Optional.of(news));

        mockMvc.perform(get("/api/news/5/similar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void getSimilarNewsByNewsIdWhereNewsTypeIsPresent() throws Exception {
        News news = new News(new NewsType("title", "desc"));
        when(newsRepository.findById(any())).thenReturn(Optional.of(news));
        when(newsRepository.getLastNewsDTOByNewsTypeIdWithLimit(any(), any(), any())).thenReturn(Page.empty());

        mockMvc.perform(get("/api/news/5/similar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }

    @Test
    void incrementViews() throws Exception {
        mockMvc.perform(post("/api/news/5/incrementViews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("5"));
        verify(newsRepository, times(1)).incrementViews(5L);
    }
}
