package gov.milove.repositories;


import gov.milove.domain.News;
import gov.milove.domain.Role;
import gov.milove.domain.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTest {

    @Autowired
    UserRepo userRepo;



    @Test
    public void test() {
        User user = userRepo.findByLogin("HelloWorld_").orElseThrow(EntityNotFoundException::new);
        System.out.println(user.getAuthority());
        System.out.println(user.getAuthorities());
//        User user = new User();
//        user.setFirst_name("Admin");
//        user.setLast_name("Admin");
//        user.setLogin("HelloWorld_");
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        user.setPassword(encoder.encode("__milove@_VG1984"));
//        List<Role> roles = new ArrayList<>();
//        roles.add(new Role("ADMIN"));
//        user.setAuthority(roles);
//        userRepo.save(user);
    }

}
