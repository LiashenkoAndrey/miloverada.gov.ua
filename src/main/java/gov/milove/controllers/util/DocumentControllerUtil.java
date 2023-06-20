package gov.milove.controllers.util;

import gov.milove.exceptions.DocumentCrudServiceException;
import gov.milove.services.document.DocumentService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static gov.milove.controllers.util.ControllerUtil.error;
import static gov.milove.controllers.util.ControllerUtil.ok;

@Controller
@RequestMapping("/document")
public class DocumentControllerUtil {

    private final DocumentService documentService;

    public DocumentControllerUtil(@Qualifier("administrationDocumentService") DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDocument(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "title",required = false) String title,
            @RequestParam("filename") String filename) {

        try {
            documentService.updateDocument(filename, file, title);
            return ok("Оновлення успішне");
        } catch (Exception ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з видаленням");
        }
    }


    @GetMapping("/delete")
    public ResponseEntity<String> deleteDocument(@RequestParam("filename") String filename) {

        try {
            documentService.deleteDocumentByFilename(filename);
            return ok("Файл успішно видалений");
        } catch (DocumentCrudServiceException ex) {
            ex.printStackTrace();
            return error("Виникли проблеми з видаленням файлу");
        }
    }
}
