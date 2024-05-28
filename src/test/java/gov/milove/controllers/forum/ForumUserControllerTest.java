package gov.milove.controllers.forum;

import gov.milove.domain.AppUser;
import gov.milove.domain.Image;
import gov.milove.domain.dto.forum.NewForumUserDto;
import gov.milove.domain.forum.ForumUser;
import gov.milove.repositories.mongo.ImageRepo;
import gov.milove.services.forum.impl.ForumUserServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static gov.milove.util.Util.decodeUriComponent;
import static gov.milove.util.Util.encodeUriComponent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
@Log4j2
class ForumUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    @Autowired
    private ForumUserServiceImpl forumUserService;

    @MockBean
    @Autowired
    private ImageRepo imageRepo;

    @Test
    void newForumUser() throws Exception {
        String imageHexId = ObjectId.get().toHexString();
        String encodedAppUserId = encodeUriComponent("33333");
        MockMultipartFile file = new MockMultipartFile("avatarImageFile", "Hello, World!".getBytes());
        NewForumUserDto forumUserDto = new NewForumUserDto(file ,"aboutMe", "userNick");

        when(imageRepo.save(any(Image.class))).thenReturn(new Image(file));
        ForumUser forumUser = ForumUser.builder()
                .nickname(forumUserDto.getNickname())
                .avatar(imageHexId)
                .registeredOn(new Date())
                .aboutMe(forumUserDto.getAboutMe())
                .appUser(new AppUser(decodeUriComponent(encodedAppUserId)))
                .build();


        when(forumUserService.saveNewUser(forumUserDto, imageHexId)).thenReturn(forumUser);

        mockMvc.perform(multipart("/api/protected/forum/user/new")
                        .file(file)
                        .param("aboutMe", forumUserDto.getAboutMe())
                        .param("nickname", forumUserDto.getNickname())
                        .param("encodedAppUserId", encodedAppUserId)
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.aboutMe").value(forumUserDto.getAboutMe()),
                        jsonPath("$.nickname").value(forumUserDto.getNickname()),
                        jsonPath("$.avatar").value(forumUserDto.getNickname()),
                        jsonPath("$.appUser.id").value(decodeUriComponent(encodedAppUserId))
                );
    }
}
