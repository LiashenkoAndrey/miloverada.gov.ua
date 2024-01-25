package gov.milove.services.forum;

import gov.milove.domain.forum.File;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    File save(MultipartFile file);

    void deleteById(Long id);

}
