package gov.milove.controllers.forum;

import gov.milove.domain.forum.mongo.MongoMessageImage;
import gov.milove.exceptions.ImageNotFoundException;
import gov.milove.repositories.mongo.MessageImageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class ForumUploadController {

    private final MessageImageRepo imageRepo;

    @GetMapping("/forum/upload/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        MongoMessageImage image = imageRepo.findById(id).orElseThrow(ImageNotFoundException::new);
        String base64 = image.getBase64Image();
        String type = base64.substring(5, base64.indexOf(";"));
        MediaType mediaType = MediaType.parseMediaType(type);
        return ResponseEntity
                .ok()
                .contentType(mediaType)
                .body(Base64.getDecoder().decode(base64.split(",")[1]));
    }
}
