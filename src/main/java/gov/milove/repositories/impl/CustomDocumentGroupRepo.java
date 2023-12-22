package gov.milove.repositories.impl;

import gov.milove.domain.dto.DocumentGroupDto;

import java.util.List;

public interface CustomDocumentGroupRepo {


    List<DocumentGroupDto> findAllDto();

}
