package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.MessageImageDto;
import gov.milove.domain.forum.MessageImage;
import gov.milove.domain.forum.MongoMessageImage;
import gov.milove.exceptions.ImageNotFoundException;
import gov.milove.repositories.mongo.MessageImageRepo;
import gov.milove.services.forum.MessageImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageImageServiceImpl implements MessageImageService {

    private final MessageImageRepo messageImageRepo;

    @Override
    public List<MessageImage> saveImages(List<MessageImageDto> dtoList) {
        return dtoList.stream()
                .map(MessageImageDto::toEntity)
                .map(this::saveIfNotExistOrGetSaved)
                .map((e) -> new MessageImage(e.getId(), e.getHashCode()))
                .toList();
    }

    private MongoMessageImage saveIfNotExistOrGetSaved(MongoMessageImage image) {
        Example<MongoMessageImage> example = Example.of(new MongoMessageImage(image.getHashCode()));
        if (messageImageRepo.exists(example)) {
            return messageImageRepo.findOne(example).orElseThrow(ImageNotFoundException::new);
        }
        return messageImageRepo.save(image);
    }
}
