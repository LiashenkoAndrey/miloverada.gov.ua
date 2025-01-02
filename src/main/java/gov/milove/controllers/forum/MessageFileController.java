package gov.milove.controllers.forum;

import com.mongodb.client.gridfs.model.GridFSFile;
import gov.milove.domain.forum.mongo.MongoFile;
import gov.milove.exceptions.FileNotFoundException;
import gov.milove.repositories.mongo.MongoFileRepo;
import gov.milove.services.forum.MessageFileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static gov.milove.util.Util.createContentDispositionHeaderFromFileName;

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

    @DeleteMapping("/protected/forum/message/file/{id}")
    public Long delete(@PathVariable Long id) {
        return messageFileService.deleteById(id);
    }

    @GetMapping("/forum/upload/largeFile/{fileId}")
    public void getLargeFile(@PathVariable String fileId, HttpServletResponse response) throws IOException {
        GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileId)));
        String type = file.getMetadata().getString("contentType");

        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, createContentDispositionHeaderFromFileName(file.getFilename()));
        response.addHeader(HttpHeaders.CONTENT_TYPE, type);
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.getLength()));
        FileCopyUtils.copy(operations.getResource(file).getInputStream(), response.getOutputStream());
    }

    @GetMapping("/forum/upload/file/{mongoFileId}")
    public ResponseEntity<byte[]>  getFile(@PathVariable String mongoFileId) {
        MongoFile file = mongoFileRepo.findById(mongoFileId).orElseThrow(FileNotFoundException::new);
        String contentDispositionHeader = createContentDispositionHeaderFromFileName(file.getName());

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(file.getContentType()))
                .contentLength(file.getSize())
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDispositionHeader)
                .body(file.getFile().getData());
    }


}
