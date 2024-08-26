package drajner.hetman.config;

import drajner.hetman.entities.UserEntity;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.status.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitialization implements ApplicationRunner {

    @Autowired
    UserRepo userRepo;

    public void run(ApplicationArguments args){

        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode("admin");
        UserEntity admin = new UserEntity("admin", encodedPassword, UserStatus.ADMIN);
        userRepo.save(admin);
    }
}
