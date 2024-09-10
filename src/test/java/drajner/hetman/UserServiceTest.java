package drajner.hetman;

import drajner.hetman.entities.UserEntity;
import drajner.hetman.errors.DuplicateException;
import drajner.hetman.repositories.UserRepo;
import drajner.hetman.requests.RegisterRequest;
import drajner.hetman.services.UserService;
import drajner.hetman.status.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserEntity testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new UserEntity();
        testUser.setUsername("testUser");
        testUser.setPassword("hashedPassword");
    }

    @Test
    public void testSearchForUserSuccess() {
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        UserEntity user = userService.searchForUser("testUser");

        verify(userRepo, times(1)).findByUsername("testUser");
        assertNotNull(user);
        assertEquals("testUser", user.getUsername());
    }

    @Test
    public void testSearchForUserFailure() {
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> userService.searchForUser("testUser"));
    }

    @Test
    public void testRegisterSuccess() throws DuplicateException {
        when(userRepo.findByUsername("newUser")).thenReturn(Optional.empty());

        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");

        RegisterRequest registerRequest = new RegisterRequest("newUser", "password");

        userService.register(registerRequest);

        verify(userRepo, times(1)).save(any(UserEntity.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    public void testRegisterFailure_DuplicateUser() {
        when(userRepo.findByUsername("testUser")).thenReturn(Optional.of(testUser));

        RegisterRequest registerRequest = new RegisterRequest("testUser", "password");

        assertThrows(DuplicateException.class, () -> userService.register(registerRequest));
    }
}
