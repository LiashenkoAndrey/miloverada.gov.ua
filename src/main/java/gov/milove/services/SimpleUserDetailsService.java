package gov.milove.services;

import gov.milove.domain.User;
import gov.milove.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SimpleUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserRepo userRepo;


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepo.findByLogin(login);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User not found!");
        }
    }
}

