package drajner.hetman;

import drajner.hetman.entities.UserEntity;
import drajner.hetman.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.nio.file.AccessDeniedException;
import java.lang.reflect.Field;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenUtilsTest {

    private Key secretKey;
    private static final int MINUTES = 60;

    @BeforeEach
    public void setUp() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    @Test
    public void testGenerateToken() throws Exception {

        Field secretKeyField = TokenUtils.class.getDeclaredField("SECRET_KEY");
        secretKeyField.setAccessible(true);
        Key secretKey = (Key) secretKeyField.get(null);

        String username = "testUser";

        String token = TokenUtils.generateToken(username);

        assertNotNull(token);
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        assertEquals(username, claims.getSubject());
        assertTrue(claims.getExpiration().after(Date.from(Instant.now())));
    }

    @Test
    public void testGetUsernameFromToken() throws Exception {
        String username = "testUser";
        String token = TokenUtils.generateToken(username);

        String extractedUsername = TokenUtils.getUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    public void testValidateTokenSuccess() throws Exception {
        String username = "testUser";
        UserEntity mockUser = mock(UserEntity.class);
        when(mockUser.getUsername()).thenReturn(username);

        String token = TokenUtils.generateToken(username);

        boolean isValid = TokenUtils.validate(token, mockUser);

        assertTrue(isValid);
    }

    @Test
    public void testValidateTokenFailure_InvalidUsername() throws Exception {
        String username = "testUser";
        UserEntity mockUser = mock(UserEntity.class);
        when(mockUser.getUsername()).thenReturn("differentUser");

        String token = TokenUtils.generateToken(username);

        boolean isValid = TokenUtils.validate(token, mockUser);

        assertFalse(isValid);
    }

    @Test
    public void testValidateTokenFailure_ExpiredToken() throws Exception {
        String username = "testUser";
        UserEntity mockUser = mock(UserEntity.class);
        when(mockUser.getUsername()).thenReturn(username);

        Instant currentTime = Instant.now().minus(1, java.time.temporal.ChronoUnit.HOURS);
        String expiredToken = Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(currentTime))
                .expiration(Date.from(currentTime.plus(MINUTES, java.time.temporal.ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();

        assertThrows(AccessDeniedException.class, () -> TokenUtils.validate(expiredToken, mockUser));
    }

    @Test
    public void testIsExpiredToken() throws Exception {
        String username = "testUser";

        Field secretKeyField = TokenUtils.class.getDeclaredField("SECRET_KEY");
        secretKeyField.setAccessible(true);
        Key secretKey = (Key) secretKeyField.get(null);

        Instant currentTime = Instant.now().minus(2, java.time.temporal.ChronoUnit.HOURS);
        String expiredToken = Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(currentTime))
                .expiration(Date.from(currentTime.plus(MINUTES, java.time.temporal.ChronoUnit.MINUTES)))
                .signWith(secretKey)
                .compact();

        UserEntity mockUser = mock(UserEntity.class);
        when(mockUser.getUsername()).thenReturn(username);

        assertThrows(AccessDeniedException.class, () -> {
            TokenUtils.validate(expiredToken, mockUser);
        });
    }

}
