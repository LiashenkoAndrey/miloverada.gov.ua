package gov.milove.controllers.documents;

import gov.milove.domain.Document;
import gov.milove.repositories.jpa.document.DocumentRepo;
import gov.milove.services.DocumentService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
@Log4j2
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentRepo documentRepo;

    @MockBean
    private DocumentService documentService;

    @Test
    void updateDocumentName() throws Exception {
        Document document = new Document(5L, "title", "name");
        when(documentRepo.findById(5L)).thenReturn(Optional.of(document));
        mockMvc.perform(put("/api/protected/document/5/update")
                        .param("name", "newName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("5"));
        verify(documentRepo, times(1)).save(any(Document.class));
    }

    @Test
    void deleteDocument() throws Exception {
        when(documentRepo.findById(anyLong())).thenReturn(Optional.of(new Document(5L , "d", "dd")));
        mockMvc.perform(delete("/api/protected/document/5/delete"))
                .andExpect(status().isOk());
        verify(documentService, times(1)).delete(any(Document.class));
    }
}
