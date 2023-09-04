package gov.milove.repositoriesTest;

import gov.milove.domain.dto.DocumentGroupDto;
import gov.milove.repositories.document.DocumentGroupRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeneralGroups {

    @Autowired
    DocumentGroupRepository repo;

    @Test
    public void get() {
        List<DocumentGroupDto> dto = repo.findGeneralGroupsDto();

        for (DocumentGroupDto d : dto) {
            System.out.println(d.getTitle());
        }

    }
}
