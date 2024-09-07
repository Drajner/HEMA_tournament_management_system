package drajner.hetman.controllers;


import drajner.hetman.errors.DuplicateException;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.requests.*;
import drajner.hetman.status.UserStatus;
import drajner.hetman.utils.TokenUtils;
import drajner.hetman.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        String token = TokenUtils.generateToken(loginRequest.getUsername());
        UserStatus userStatus = userService.getUserStatus(loginRequest.getUsername());
        return ResponseEntity.ok(new LoginResponse(token, userStatus));
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
        try {
            userService.register(registerRequest);
            return ResponseEntity.ok().build();
        }catch(Exception e){
            log.error(e);
            ErrorResponse response = new ErrorResponse(e.getClass().getSimpleName(), e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

}