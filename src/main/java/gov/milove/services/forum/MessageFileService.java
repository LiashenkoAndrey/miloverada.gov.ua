package gov.milove.services.forum;

import gov.milove.domain.forum.MessageFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MessageFileService {


    Long deleteById(Long id);

    MessageFile save(MultipartFile file, Long messageId);

    void saveFiles(MultipartFile[] files, Long messageId, Long chatId);

    void saveFile(MultipartFile files, Long messageId, Long chatId);
}
