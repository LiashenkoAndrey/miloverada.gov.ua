package gov.milove.controllers.forum;

import com.mongodb.client.gridfs.model.GridFSFile;
import gov.milove.domain.forum.File;
import gov.milove.domain.forum.MessageFile;
import gov.milove.domain.forum.MongoFile;
import gov.milove.exceptions.FileNotFoundException;
import gov.milove.repositories.forum.MessageFileRepo;
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
import org.springframework.http.HttpHeaders;
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
    private final MessageFileRepo messageFileRepo;

    @PostMapping("/protected/forum/chat/{chatId}/message/{messageId}/files")
    public void saveFiles(@RequestParam("files") MultipartFile[] files, @PathVariable Long messageId, @PathVariable Long chatId) {
        messageFileService.saveFiles(files, chatId, messageId);

    }

    @PostMapping("/protected/forum/chat/{chatId}/message/{messageId}/file")
    public void saveFile(@RequestParam MultipartFile file, @PathVariable Long messageId, @PathVariable Long chatId) {
        messageFileService.saveFile(file, messageId, chatId);
    }

    @DeleteMapping("/protected/forum/message/file/{id}")
    public Long delete(@PathVariable Long id) {
        return messageFileService.deleteById(id);
    }

    @GetMapping("/forum/upload/largeFile/{fileId}")
    public void getLargeFile(@PathVariable String fileId, HttpServletResponse response) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        String type = file.getMetadata().getString("contentType");
        log.info("file is present = {}, large file type = {}, length = {}", file.getFilename(), type, file.getLength());

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, getContentDispositionHeader(file.getFilename()));
        response.addHeader(HttpHeaders.CONTENT_TYPE, type);
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getLength()));
        FileCopyUtils.copy(operations.getResource(file).getInputStream(), response.getOutputStream());
    }

    @GetMapping("/forum/upload/file/{mongoFileId}")
    public ResponseEntity<byte[]>  getFile(@PathVariable String mongoFileId) {

        MongoFile file = mongoFileRepo.findById(mongoFileId).orElseThrow(FileNotFoundException::new);

        log.info("file contentType = {}", MediaType.valueOf(file.getContentType()));

        String contentDispositionHeader = getContentDispositionHeader(file.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getContentType()))
                .contentLength(file.getSize())
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDispositionHeader)
                .body(file.getFile().getData());
    }

    private String getContentDispositionHeader(String fileName) {
        return String.format("attachment; filename=\"%s\"", fileName);
    }

}
