package gov.milove.controllers;

import gov.milove.services.document.DocumentService;
import gov.milove.services.ImageService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/upload")
public class UploadController {
    private final ImageService imageService;


    private final DocumentService documentService;

    public UploadController(ImageService imageService, @Qualifier("administrationDocumentService") DocumentService documentService) {
        this.imageService = imageService;
        this.documentService = documentService;
    }

    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String savedImageId = imageService.saveImage(file);
        if (savedImageId.equals("error")) return ResponseEntity.internalServerError().body("error");
        else return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(savedImageId);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        byte[] image = imageService.getImageById(id);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(image);
    }

    @GetMapping("/document/{id}")
    public ResponseEntity<byte[]> getDocument(@PathVariable("id") String id) {
        byte[] file = documentService.getDocumentBinaryById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-disposition", "inline; filename=\"" + id + "\"");
        headers.set("content-length", String.valueOf(file.length));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .headers(headers)
                .body(file);
    }
}
