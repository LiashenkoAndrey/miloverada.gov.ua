package gov.milove.services;

import gov.milove.repositories.ImageRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {

    private final ImageRepositoryImpl imageRepository;

    public ImageService(ImageRepositoryImpl imageRepository) {
        this.imageRepository = imageRepository;
    }

    public String saveImage(MultipartFile file) {
        return imageRepository.saveImage(file);
    }

    public byte[] getImageById(String id) {
        return imageRepository.getImageById(id);
    }


}
