package gov.milove.controllers;

import gov.milove.domain.TextBanner;
import gov.milove.repositories.TextBannerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TextBannerControllerTest extends BannerControllerTest {

    @Autowired
    private TextBannerRepository repo;

    @BeforeAll
    public void createBanner() {
        TextBanner banner = TextBanner.builder()
                .id(1L)
                .mainText("main text")
                .description("desc")
                .build();
        repo.save(banner);
    }



    @Test
    @Order(1)
    public void create() throws Exception {
        TextBanner textBanner = TextBanner.builder()
                .mainText("text")
                .description("main desc")
                .build();

        String jsonContent = mapper.writeValueAsString(textBanner);

        mockMvc.perform(post("/text-banner/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    long savedId = Long.parseLong(result.getResponse().getContentAsString());
                    TextBanner saved = repo.findById(savedId).orElseThrow(EntityNotFoundException::new);
                    assertEquals(textBanner.getMainText(), saved.getMainText());
                    assertEquals(textBanner.getDescription(), saved.getDescription());
                });

        mockMvc.perform(post("/text-banner/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(2)
    public void updateTextBanner() throws Exception {
        TextBanner banner = TextBanner.builder()
                .mainText("new text")
                .id(1L)
                .build();

        mockMvc.perform(put("/text-banner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(banner)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    TextBanner saved = repo.findById(1L).orElseThrow(EntityNotFoundException::new);
                    assertEquals(banner.getMainText(), saved.getMainText());
                    assertNotEquals(banner.getDescription(), saved.getDescription());
                });

        banner.setId(null);

        mockMvc.perform(put("/text-banner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(banner)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void deleteBanner() throws Exception {
        assertTrue(repo.existsById(1L));
        mockMvc.perform(delete("/text-banner/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertFalse(repo.existsById(1L)));

        mockMvc.perform(delete("/text-banner/delete"))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    @Transactional
    public void deleteAfter() {
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("truncate table milove.public.text_banner").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE milove.public.text_banner_id_seq RESTART WITH 1").executeUpdate();
        transaction.commit();
    }

}
