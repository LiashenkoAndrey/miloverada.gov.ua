package gov.milove.domain.dto;


import java.util.Date;
import java.util.List;

public interface DocumentGroupWithGroupsDto {

    String getName();

    Long getId();

    Date getCreatedOn();

    List<DocumentGroupWithGroupsDto> getGroups();
}
