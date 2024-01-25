package gov.milove.controllers.forum;

import com.mongodb.client.gridfs.model.GridFSFile;
import gov.milove.domain.forum.MessageFile;
import gov.milove.domain.forum.MongoFile;
import gov.milove.exceptions.FileNotFoundException;
import gov.milove.repositories.mongo.MongoFileRepo;
import gov.milove.services.forum.MessageFileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@RestController
@Log4j2
@RequestMapping("/api/")
@RequiredArgsConstructor
public class MessageFileController {

    private final MessageFileService messageFileService;
    private final MongoFileRepo mongoFileRepo;
    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations operations;

    @PostMapping("/protected/forum/chat/{chatId}/message/{messageId}/files")
    public void saveFiles(@RequestParam("files") MultipartFile[] files, @PathVariable Long messageId, @PathVariable Long chatId) {
        messageFileService.saveFiles(files, chatId, messageId);

    }

    @PostMapping("/protected/forum/chat/{chatId}/message/{messageId}/file")
    public void saveFile(@RequestParam MultipartFile file, @PathVariable Long messageId, @PathVariable Long chatId) {
        messageFileService.saveFile(file, messageId, chatId);
    }

    @DeleteMapping("/protected/forum/message/file/{id}/delete")
    public Long delete(@PathVariable Long id) {

    }

    @GetMapping("/forum/upload/largeFile/{fileId}")
    public void getLargeFile(@PathVariable String fileId, HttpServletResponse response) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        if (file != null) {
            log.info("file is present = {}", file.getFilename());
            FileCopyUtils.copy(operations.getResource(file).getInputStream(), response.getOutputStream());
        }
        throw new FileNotFoundException(fileId);
    }

    @GetMapping("/forum/upload/file/{fileId}")
    public ResponseEntity<byte[]>  getFile(@PathVariable String fileId) {

        MongoFile mongoFile = mongoFileRepo.findById(fileId).orElseThrow(FileNotFoundException::new);
        log.info("file contentType = {}", MediaType.valueOf(mongoFile.getContentType()));
        return ResponseEntity.ok().contentType(MediaType.valueOf(mongoFile.getContentType()))

                .body(mongoFile.getFile().getData());
    }

}
