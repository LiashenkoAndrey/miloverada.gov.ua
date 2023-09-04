package gov.milove.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import gov.milove.repositories.LinkBannerRepository;
import gov.milove.repositories.TextBannerRepository;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;



@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BannerControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @PersistenceUnit
    protected EntityManagerFactory factory;

    protected final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private LinkBannerRepository linkBannerRepository;

    @Autowired
    private TextBannerRepository textBannerRepository;

}
