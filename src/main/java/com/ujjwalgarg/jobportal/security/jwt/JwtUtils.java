package com.ujjwalgarg.jobportal.security.jwt;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.ujjwalgarg.jobportal.security.services.UserDetailsImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for generating and validating JWT tokens.
 * This class provides methods for creating JWT tokens based on user
 * authentication information,
 * extracting user email from a token, and validating the integrity and
 * expiration of a token.
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${ujjwal.app.jwtSecret}")
    private String jwtSecret;

    @Value("${ujjwal.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * Generates a JWT token based on the provided authentication information.
     *
     * @param authentication the authentication object containing user details.
     * @return a JWT token string.
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    /**
     * Generates a secret key based on the jwtSecret property.
     *
     * @return the generated secret key.
     */
    private SecretKey key() {
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
    }

    /**
     * Extracts the user email (subject) from the given JWT token.
     *
     * @param token the JWT token.
     * @return the email extracted from the JWT token.
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Validates the given JWT token.
     * Checks the token's integrity, expiration, and format.
     *
     * @param authToken the JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(new SecretKeySpec(key().getEncoded(), key().getAlgorithm()))
                    .build()
                    .parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

}
