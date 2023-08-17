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

    public String saveAndReturnId(MultipartFile file) {
        return imageRepository.saveImage(file);
    }

    public byte[] getImageById(String id) {
        return imageRepository.getImageById(id);
    }

    public void updateImageIfPresent(MultipartFile file, String image_id) {
        if (file != null) {
            imageRepository.updateImage(file, image_id);
        }
    }

    public void deleteImageById(String id) {
        imageRepository.deleteImageById(id);
    }
}
