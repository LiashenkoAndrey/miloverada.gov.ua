package gov.milove.repositoriesTest;

import gov.milove.domain.LinkBanner;
import gov.milove.repositories.LinkBannerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class LinkBannerRepositoryTest extends BannerRepositoryTest {

    @Autowired
    private LinkBannerRepository repo;

    @BeforeAll
    public void before() {
        LinkBanner banner = LinkBanner.builder()
                .id(1L)
                .url("https://docs.jboss.org/hibernate/stable/core/reference/en/html/mapping.html#mapping-types-custom")
                .text("default text")
                .build();

        repo.save(banner);
    }

    @Test
    @Order(1)
    public void create() {
        String url = ("https://stackoverflow.com/questions/221611/creation-timestamp-and-last-update-timestamp-with-hibernate-and-mysql");
        String text = "text";
        LinkBanner banner = LinkBanner.builder()
                .text(text)
                .url(url)
                .build();

        LinkBanner saved = repo.save(banner);
        assertEquals(url, saved.getUrl());
        assertEquals(text, saved.getText());
    }

    @Test
    @Order(2)
    public void get() {
        Optional<LinkBanner> saved = repo.findById(1L);
        assertTrue(saved.isPresent());
    }

    @Test
    @Order(3)
    public void update() {
        String text = "text";
        String url = "https://stackoverflow.com/questions/221611";
        LinkBanner banner = LinkBanner.builder()
                .text(text)
                .url(url)
                .build();

        LinkBanner saved = repo.save(banner);
        saved.setText("new text");
        saved.setUrl("https://new-url.com");
        LinkBanner updated = repo.save(saved);

        assertNotEquals(text, updated.getText());
        assertNotEquals(url, updated.getUrl());
    }

    @Test
    @Order(4)
    public void delete() {
        Long id = 1L;
        assertTrue(repo.existsById(id));
        repo.deleteById(id);
        assertFalse(repo.existsById(id));
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
