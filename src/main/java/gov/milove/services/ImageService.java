package gov.milove.services;

import gov.milove.repositories.ImageRepository;
import gov.milove.repositories.impl.ImageRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    public ImageService(ImageRepositoryImpl imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String saveImage(MultipartFile file) {
        return imageRepository.saveImage(file);
    }

    public byte[] getImageById(String id) {
        return imageRepository.getImageById(id);
    }

    public void updateImage(MultipartFile file, String image_id) {
        imageRepository.updateImage(image_id, file);
    }

    public void deleteImageById(String id) {
        imageRepository.deleteImageById(id);
    }
}
