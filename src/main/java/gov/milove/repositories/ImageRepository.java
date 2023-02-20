package gov.milove.repositories;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {

    public String saveImage(MultipartFile file);

    public byte[] getImageById(String id);
}
