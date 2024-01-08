package gov.milove.controllers.forum;

import gov.milove.services.forum.MessageImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Log4j2
public class MessageImageController {

    private final MessageImageService messageImageService;

    @PutMapping("/protected/forum/messageImage/{id}/update")
    public void updateOneImage(@PathVariable Long id, @RequestParam String base64Image) {

    }

    @DeleteMapping("/protected/forum/message/{messageId}/image/{imageId}/delete")
    public void deleteMessageImageById(@PathVariable String imageId, @PathVariable Long messageId) {
        log.info("DELETE MESSAGE IMAGE imageId={}, messageId={}", imageId, messageId);
        messageImageService.deleteImageFromMessage(imageId, messageId);
    }
}
