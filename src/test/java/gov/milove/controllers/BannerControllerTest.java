package gov.milove.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.milove.domain.Banner;
import gov.milove.repositories.BannerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceUnit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BannerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BannerRepository repository;

    @PersistenceUnit
    private EntityManagerFactory factory;

    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeAll
    public void createBanner() {
        Banner banner = Banner.builder()
                .id(1L)
                .mainText("main text")
                .description("description")
                .createdOn(LocalDate.now())
                .build();
        repository.save(banner);
    }

    @Test
    public void createView() throws Exception {
        mockMvc.perform(get("/banner/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("/banner/new"));
    }

    @Test
    public void create() throws Exception {
        Banner banner = Banner.builder()
                .createdOn(LocalDate.now())
                .description("description1")
                .mainText("main text1")
                .build();

        String jsonContent = mapper.writeValueAsString(banner);

        mockMvc.perform(post("/banner/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    long savedId = Long.parseLong(result.getResponse().getContentAsString());
                    Banner saved = repository.findById(savedId)
                            .orElseThrow(EntityNotFoundException::new);
                    assertEquals(banner.getDescription(), saved.getDescription());
                    assertEquals(banner.getMainText(), saved.getMainText());
                });

        mockMvc.perform(post("/banner/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isConflict());
    }


    @Test
    public void updateView() throws Exception {
        mockMvc.perform(get("/banner/update")
                        .param("id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/banner/update"));
    }

    @Test
    public void updateBanner() throws Exception {
        System.out.println(repository.findAll());
        Banner banner = repository.findById(1L).orElseThrow(EntityNotFoundException::new);
        banner.setDescription("new description");

        mockMvc.perform(put("/banner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(banner)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    Banner saved = repository.findById(1L).orElseThrow(EntityNotFoundException::new);
                    assertEquals(banner.getDescription(), saved.getDescription());
                });

        banner.setId(null);

        mockMvc.perform(put("/banner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(banner)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteBanner() throws Exception {
        assertTrue(repository.existsById(1L));
        mockMvc.perform(delete("/banner/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertFalse(repository.existsById(1L)));

        mockMvc.perform(delete("/banner/delete")
                        .param("id", "1"))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    @Transactional
    public void deleteAfter() {
        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createNativeQuery("truncate table milove.public.banner");
        entityManager.getTransaction().commit();
    }


}
