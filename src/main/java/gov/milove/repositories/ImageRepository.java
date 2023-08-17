package gov.milove.repositories;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {

    public String saveImage(MultipartFile file);

    public void updateImage(MultipartFile file, String image_id);

    public byte[] getImageById(String id);

    public void deleteImageById(String id);
}
