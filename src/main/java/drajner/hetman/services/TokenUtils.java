package drajner.hetman.services;

import drajner.hetman.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.nio.file.AccessDeniedException;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class TokenUtils {

    private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final int MINUTES = 60;

    public static String generateToken(String username){
        Instant currentTime = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(currentTime))
                .expiration(Date.from(currentTime.plus(MINUTES, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private static Claims getTokenBody(String token) throws AccessDeniedException{
        try{
            return Jwts.parser()
                    .verifyWith((SecretKey) SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }catch(SignatureException | ExpiredJwtException e){
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }

    private static boolean isExpired(String token) throws AccessDeniedException{
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }

    public static String getUsername(String token) throws AccessDeniedException{
        return getTokenBody(token).getSubject();
    }

    public static boolean validate(String token, UserEntity userEntity) throws AccessDeniedException{
        String username = getUsername(token);
        return username.equals(userEntity.getUsername()) && !isExpired(token);
    }

}
