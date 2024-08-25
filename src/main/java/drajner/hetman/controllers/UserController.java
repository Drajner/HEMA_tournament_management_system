package drajner.hetman.controllers;


import drajner.hetman.entities.UserEntity;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.repositories.SessionRepo;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.requests.*;
import drajner.hetman.services.TokenUtils;
import drajner.hetman.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin(origins = {"http://localhost:5173"}, allowCredentials = "true")
@RequestMapping("/users")
@Log4j2
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SessionRepo sessionRepo;

    /*
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

     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String token = TokenUtils.generateToken(loginRequest.getUsername());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<Object> getUsers()
    {
        try {
            return ResponseEntity.ok(userRepo.findAll());
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest registerRequest) throws DuplicateException
    {
        //try {
            userService.register(registerRequest);
            return ResponseEntity.ok().build();
        /*}catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

         */
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