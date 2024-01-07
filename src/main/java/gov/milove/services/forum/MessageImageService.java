package gov.milove.services.forum;

import gov.milove.domain.dto.forum.MessageImageDto;
import gov.milove.domain.forum.MessageImage;

import java.util.List;

public interface MessageImageService {

    List<MessageImage> saveImages(List<MessageImageDto> dtoList);

}
