package gov.milove.controllers;

import gov.milove.domain.LinkBanner;
import gov.milove.repositories.LinkBannerRepository;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class LinkBannerControllerTest extends BannerControllerTest {

    @Autowired
    private LinkBannerRepository repo;

    @BeforeAll
    public void createBanner() {
        LinkBanner banner = LinkBanner.builder()
                .id(1L)
                .url("")
                .text("text")
                .build();
        repo.save(banner);
    }


    @Test
    @Order(1)
    public void create() throws Exception {
        LinkBanner linkBanner = LinkBanner.builder()
                .url("https://docs.jboss.org")
                .text("main text1")
                .build();

        String jsonContent = mapper.writeValueAsString(linkBanner);

        mockMvc.perform(post("/link-banner/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    long savedId = Long.parseLong(result.getResponse().getContentAsString());
                    LinkBanner saved = repo.findById(savedId).orElseThrow(EntityNotFoundException::new);
                    assertEquals(linkBanner.getUrl(), saved.getUrl());
                    assertEquals(linkBanner.getText(), saved.getText());
                });

        mockMvc.perform(post("/link-banner/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(2)
    public void updateLinkBanner() throws Exception {
        LinkBanner banner = repo.findById(1L).orElseThrow(EntityNotFoundException::new);
        banner.setText("new text");

        mockMvc.perform(put("/link-banner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(banner)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    LinkBanner saved = repo.findById(1L).orElseThrow(EntityNotFoundException::new);
                    assertEquals(banner.getText(), saved.getText());
                });

        banner.setId(null);

        mockMvc.perform(put("/link-banner/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(banner)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(3)
    public void deleteBanner() throws Exception {
        assertTrue(repo.existsById(1L));
        mockMvc.perform(delete("/link-banner/delete")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(result -> assertFalse(repo.existsById(1L)));

        mockMvc.perform(delete("/link-banner/delete"))
                .andExpect(status().isBadRequest());
    }

    @AfterAll
    @Transactional
    public void deleteAfter() {
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();
        em.createNativeQuery("truncate table milove.public.link_banner").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE milove.public.link_banner_id_seq RESTART WITH 1").executeUpdate();
        transaction.commit();
    }
}
