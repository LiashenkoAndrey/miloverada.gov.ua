package gov.milove.services.forum;

import gov.milove.domain.dto.forum.MessageImageDto;
import gov.milove.domain.forum.message.MessageImage;

import java.util.List;

public interface MessageImageService {


    List<MessageImage> saveImages(List<MessageImageDto> dtoList);

    void deleteImagesIfNotUsedMoreThenOneTime(List<MessageImage> images);

    void deleteImageFromMessage(String imageId, Long messageId);
}
