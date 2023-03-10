package gov.milove.repositories;

import org.springframework.web.multipart.MultipartFile;

public interface ImageRepository {

    public String saveImage(MultipartFile file);

    public void updateImage(String image_id, MultipartFile file);

    public byte[] getImageById(String id);

    public void deleteImageById(String id);
}
