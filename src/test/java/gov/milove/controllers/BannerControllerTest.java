package gov.milove.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.milove.domain.Banner;
import gov.milove.repositories.BannerRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        System.out.println(repository.findAll());
    }

    @Test
    @Order(1)
    public void createView() throws Exception {
        mockMvc.perform(get("/banner/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("/banner/new"));
    }

    @Test
    @Order(2)
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
    @Order(3)
    public void updateView() throws Exception {
        System.out.println(repository.findAll());
        mockMvc.perform(get("/banner/update")
                        .param("id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/banner/update"));
    }

    @Test
    @Order(4)
    public void pageView() throws Exception {
        System.out.println(repository.findAll());
        mockMvc.perform(get("/banner/view")
                        .param("id", "1")
                )
                .andExpect(status().isOk())
                .andExpect(view().name("/banner/page"));
    }

    @Test
    @Order(5)
    public void updateBanner() throws Exception {
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
    @Order(6)
    public void deleteBanner() throws Exception {
        assertTrue(repository.existsById(1L));
        mockMvc.perform(delete("/banner/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertFalse(repository.existsById(1L)));

        mockMvc.perform(delete("/banner/delete"))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    @Transactional
    public void deleteAfter() {
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("truncate table milove.public.banner").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE milove.public.banner_id_seq RESTART WITH 1").executeUpdate();
        transaction.commit();
    }


}
