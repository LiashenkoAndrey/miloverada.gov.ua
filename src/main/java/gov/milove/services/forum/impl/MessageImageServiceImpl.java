package gov.milove.services.forum.impl;

import gov.milove.domain.dto.forum.MessageImageDto;
import gov.milove.domain.forum.message.MessageImage;
import gov.milove.domain.forum.mongo.MongoMessageImage;
import gov.milove.exceptions.ImageNotFoundException;
import gov.milove.repositories.jpa.forum.MessageRepo;
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

    private final MessageRepo messageRepo;

    @Override
    public List<MessageImage> saveImages(List<MessageImageDto> dtoList) {
        return dtoList.stream()
                .map(MessageImageDto::toEntity)
                .map(this::saveIfNotExistOrGetSaved)
                .map((e) -> new MessageImage(e.getId(), e.getHashCode()))
                .toList();
    }

    @Override
    public void deleteImagesIfNotUsedMoreThenOneTime(List<MessageImage> images) {
        for (MessageImage m : images) {
            deleteImageIfNotUsedMoreThenOneTime(m.getImageId());
        }
    }

    @Override
    public void deleteImageFromMessage(String imageId, Long messageId) {
        if (!messageRepo.messageImageIsUsedMoreThenOneTime(imageId)) {
            messageImageRepo.deleteById(imageId);
        }
        messageRepo.deleteMessageImage(imageId, messageId);
    }

    public void deleteImageIfNotUsedMoreThenOneTime(String imgId) {
        if (!messageRepo.messageImageIsUsedMoreThenOneTime(imgId)) {
            messageImageRepo.deleteById(imgId);
        }
    }

    private MongoMessageImage saveIfNotExistOrGetSaved(MongoMessageImage image) {
        Example<MongoMessageImage> example = Example.of(new MongoMessageImage(image.getHashCode()));
        if (messageImageRepo.exists(example)) {
            return messageImageRepo.findOne(example).orElseThrow(ImageNotFoundException::new);
        }
        return messageImageRepo.save(image);
    }
}
