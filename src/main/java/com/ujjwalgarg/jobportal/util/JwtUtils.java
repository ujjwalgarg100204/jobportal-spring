package com.ujjwalgarg.jobportal.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import io.jsonwebtoken.security.SignatureException;
import java.security.Key;
import java.util.Date;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtils {

  @Value("${ujjwal.app.jwtSecret}")
  private String jwtSecret;

  @Value("${ujjwal.app.jwtExpirationMs}")
  private int jwtExpirationMs;

  private SecretKey key() {
    Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
  }

  public String generateJwtToken(String subject) {
    Date issueDate = new Date();
    Date expirationDate = new Date(issueDate.getTime() + jwtExpirationMs);

    return Jwts.builder()
        .subject(subject)
        .issuedAt(issueDate)
        .expiration(expirationDate)
        .signWith(key())
        .compact();
  }

  public String getSubjectOfJwtToken(String token) {
    return Jwts.parser()
        .verifyWith(key())
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public boolean isValidJwtToken(String authToken) {
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
    } catch (SignatureException e) {
      log.error("JWT signature does not match: {}", e.getMessage());
    } catch (SecurityException e) {
      log.error("JWT security exception: {}", e.getMessage());
    } catch (Exception e) {
      log.error("Cannot validate JWT token: {}", e.getMessage());
    }

    return false;
  }
}
