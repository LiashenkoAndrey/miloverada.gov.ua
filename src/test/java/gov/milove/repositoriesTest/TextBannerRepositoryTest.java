package gov.milove.repositoriesTest;

import gov.milove.domain.TextBanner;
import gov.milove.repositories.TextBannerRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TextBannerRepositoryTest extends BannerRepositoryTest {

    @Autowired
    private TextBannerRepository repo;

    @BeforeAll
    public void before() {
        TextBanner banner = TextBanner.builder()
                .id(1L)
                .description("desc")
                .mainText("main")
                .build();

        repo.save(banner);
    }

    @Test
    @Order(1)
    public void create() {
        String desc = "desc";
        String mainText = "main text";
        TextBanner banner = TextBanner.builder()
                .mainText(mainText)
                .description(desc)
                .build();

        TextBanner saved = repo.save(banner);
        assertEquals(desc, saved.getDescription());
        assertEquals(mainText, saved.getMainText());
    }

    @Test
    @Order(2)
    public void get() {
        Optional<TextBanner> saved = repo.findById(1L);
        assertTrue(saved.isPresent());
    }

    @Test
    @Order(3)
    public void update() {
        String desc = "plain desc";
        String mainText = " plain main text";
        TextBanner banner = TextBanner.builder()
                .description(desc)
                .mainText(mainText)
                .build();

        TextBanner saved = repo.save(banner);
        saved.setDescription("new desc");
        saved.setMainText("sdfsdfdfsf");
        TextBanner updated = repo.save(saved);

        assertNotEquals(desc, updated.getDescription());
        assertNotEquals(mainText, updated.getMainText());
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
        em.createNativeQuery("truncate table milove.public.text_banner").executeUpdate();
        em.createNativeQuery("ALTER SEQUENCE milove.public.text_banner_id_seq RESTART WITH 1").executeUpdate();
        transaction.commit();
    }
}
