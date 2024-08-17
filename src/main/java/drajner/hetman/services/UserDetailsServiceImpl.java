package drajner.hetman.services;

import drajner.hetman.entities.UserEntity;
import drajner.hetman.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username){

        UserEntity user = userService.searchForUser(username);

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername()).password(user.getPassword()).build();
    }
}
