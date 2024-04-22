package drajner.hetman.controllers;


import drajner.hetman.entities.SessionEntity;
import drajner.hetman.entities.UserEntity;
import drajner.hetman.repositories.SessionRepo;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.requests.LoginRequest;
import drajner.hetman.requests.LoginResponse;
import drajner.hetman.requests.RegisterRequest;
import drajner.hetman.requests.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController("/login")
public class LoginController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    SessionRepo sessionRepo;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest requestBody)
    {
        Optional<UserEntity> user = userRepo.findByUsername(requestBody.getUsername());
        if (!user.isPresent() || !user.get().getPassword().equals(requestBody.getPassword()))
            return new LoginResponse(false, "");

        if (user.get().isAdmin() != requestBody.isAdmin())
            return new LoginResponse(false, "");

        String token = generateToken();
        SessionEntity entity = new SessionEntity(token, user.get().getUserId());
        sessionRepo.save(entity);

        return new LoginResponse(true, token);
    }

    @GetMapping("/getUsers")
    public List<UserEntity> getUsers()
    {
        List<UserEntity> users = userRepo.findAll();
        return users;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest requestBody)
    {
        Optional<UserEntity> user = userRepo.findByUsername(requestBody.getUsername());
        if (user.isPresent())
            return new RegisterResponse(false);

        UserEntity newUser = new UserEntity(requestBody.getUsername(), requestBody.getPassword());
        userRepo.save(newUser);

        return new RegisterResponse(true);
    }

    private String generateToken()
    {
        StringBuilder sessionStr = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 254; ++i)
        {
            int randVal = random.nextInt(10);

            sessionStr.append(randVal);
        }
        return sessionStr.toString();
    }
}