package gov.milove.controllers;

import gov.milove.domain.MongoNewsImage;
import gov.milove.exceptions.ImageNotFoundException;
import gov.milove.repositories.mongo.NewsImagesMongoRepo;
import gov.milove.services.document.DocumentService;
import gov.milove.services.ImageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/upload")
@Log4j2
@RequiredArgsConstructor
public class Upload {

    private final ImageService imageService;
    private final NewsImagesMongoRepo newsImagesMongoRepo;



    @PostMapping("/image")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String savedImageId = imageService.saveAndReturnId(file);
        if (savedImageId.equals("error")) return ResponseEntity.internalServerError().body("error");
        else return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(savedImageId);
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable String id) {
//        byte[] image = imageService.getImageById(id);
        MongoNewsImage mongoNewsImage = newsImagesMongoRepo.findById(id).orElseThrow(ImageNotFoundException::new);
        MediaType mediaType;
        if (mongoNewsImage.getContentType() == null) {
            mediaType = MediaType.IMAGE_PNG;
        } else  {
            mediaType = MediaType.parseMediaType(mongoNewsImage.getContentType());
        }
        log.info("image = {} ,end media type = {}", mongoNewsImage, mediaType);
        return ResponseEntity.ok().contentType(mediaType).body(mongoNewsImage.getBinaryImage().getData());
    }



//    @GetMapping("/document/{id}")
//    public ResponseEntity<byte[]> getDocument(@PathVariable("id") String id) {
//        byte[] file = documentService.getDocumentBinaryById(id);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Access-Control-Allow-Origin", "*");
//        headers.set("content-disposition", "inline");
//        headers.set("content-length", String.valueOf(file.length));
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .headers(headers)
//                .body(file);
//    }
}
