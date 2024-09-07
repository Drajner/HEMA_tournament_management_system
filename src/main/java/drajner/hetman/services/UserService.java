package drajner.hetman.services;

import drajner.hetman.entities.TournamentEntity;
import drajner.hetman.entities.UserEntity;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.requests.RegisterRequest;
import drajner.hetman.status.UserStatus;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Log4j2
public class UserService {
    @Autowired
    UserRepo userRepo;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntity searchForUser(String username){
        Optional<UserEntity> selectedUser = userRepo.findByUsername(username);
        if (selectedUser.isEmpty()) throw new NoSuchElementException("No tournament of this ID exists");
        return selectedUser.get();
    }

    public void register(RegisterRequest registerRequest) throws DuplicateException{
        Optional<UserEntity> selectedUser = userRepo.findByUsername(registerRequest.getUsername());
        if (selectedUser.isPresent()) {
            throw new DuplicateException(String.format("User with the username '%s' already exists.", registerRequest.getUsername()));
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.getPassword());
        UserEntity user = new UserEntity(registerRequest.getUsername(), hashedPassword);
        userRepo.save(user);
        log.info(String.format("Registered user %s", user.getUsername()));
    }

    public UserStatus getUserStatus(String username){
        return searchForUser(username).getUserStatus();
    }
}
