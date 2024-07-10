package com.ujjwalgarg.jobportal.util;


import static org.junit.jupiter.api.Assertions.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class JwtUtilsTest {

  @InjectMocks
  private JwtUtils jwtUtils;

  private final String testSecret = Base64.getEncoder()
      .encodeToString("B6YL8ZcFm03yFLVrmGJkrwsRSNo0Y6sdWw5bZ1apuLI=".getBytes());
  private final int testExpirationMs = 60000; // 1 minute

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(jwtUtils, "jwtSecret", testSecret);
    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", testExpirationMs);
  }

  @Test
  @DisplayName("Test generateJwtToken() should generate a valid JWT token")
  void testGenerateJwtToken() {
    String subject = "testUser";
    String token = jwtUtils.generateJwtToken(subject);

    assertNotNull(token);
    assertTrue(jwtUtils.isValidJwtToken(token));
    assertEquals(subject, jwtUtils.getSubjectOfJwtToken(token));
  }

  @Test
  @DisplayName("Test getSubjectOfJwtToken() should parse a valid JWT token and return the original subject")
  void testGetSubjectOfJwtToken_ShouldParseGivenTokenAndReturnOriginalSubject() {
    String subject = "testUser";
    String token = jwtUtils.generateJwtToken(subject);

    assertEquals(subject, jwtUtils.getSubjectOfJwtToken(token));
  }

  @Test
  @DisplayName("Test getSubjectOfJwtToken() should return null for an invalid JWT token")
  void testIsValidJwtToken_ValidToken() {
    String token = jwtUtils.generateJwtToken("testUser");
    assertTrue(jwtUtils.isValidJwtToken(token));
  }

  @Test
  @DisplayName("Test isValidJwtToken() should return false for an invalid JWT token")
  void testIsValidJwtToken_InvalidToken() {
    assertFalse(jwtUtils.isValidJwtToken("invalidToken"));
  }

  @Test
  @DisplayName("Test isValidJwtToken() should return false for an expired JWT token")
  void testIsValidJwtToken_ExpiredToken() throws InterruptedException {
    ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 1); // Set expiration to 1 ms
    String token = jwtUtils.generateJwtToken("testUser");
    Thread.sleep(10); // Wait for token to expire
    assertFalse(jwtUtils.isValidJwtToken(token));
  }

  @Test
  @DisplayName("Test isValidJwtToken() should return false for a modified JWT token")
  void testIsValidJwtToken_ModifiedToken() {
    String token = jwtUtils.generateJwtToken("testUser");
    String modifiedToken =
        token.substring(0, token.length() - 1) + "X"; // Modify the last character
    assertFalse(jwtUtils.isValidJwtToken(modifiedToken));
  }

  @Test
  @DisplayName("Test generateJwtToken() should set the expiration date correctly")
  void testTokenExpiration() {
    String subject = "testUser";
    String token = jwtUtils.generateJwtToken(subject);

    Date expirationDate = Jwts.parser()
        .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(testSecret)))
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration();

    long expectedExpiration = System.currentTimeMillis() + testExpirationMs;
    long actualExpiration = expirationDate.getTime();

    // Allow for a small difference due to processing time
    assertTrue(Math.abs(expectedExpiration - actualExpiration) < 1000);
  }
}
