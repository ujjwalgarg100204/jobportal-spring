package com.ujjwalgarg.jobportal.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalgarg.jobportal.config.SecurityContextControllerTest;
import com.ujjwalgarg.jobportal.controller.payload.auth.LoginRequest;
import com.ujjwalgarg.jobportal.service.AuthService;
import java.util.stream.Stream;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(AuthController.class)
class AuthControllerTest extends SecurityContextControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthService authService;

  @Autowired
  private ObjectMapper objectMapper;

  private static Stream<Arguments> invalidLoginRequestProvider() {
    return Stream.of(
        Arguments.of("", "password123"),
        Arguments.of("test@example.com", ""),
        Arguments.of("", ""),
        Arguments.of(null, null)
    );
  }

  @Test
  @DisplayName("Test loginUser() when given valid credentials, returns token and OK status")
  void loginUser_WithValidCredentials_ReturnsTokenAndOkStatus() throws Exception {
    LoginRequest validLoginRequest = new LoginRequest("test@example.com", "password123");
    String expectedToken = "valid.jwt.token";
    when(authService.loginUser(validLoginRequest.email(), validLoginRequest.password())).thenReturn(
        expectedToken);

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validLoginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").value(expectedToken))
        .andExpect(jsonPath("$.message", CoreMatchers.isA(String.class)));
  }

  @Test
  @DisplayName("Test loginUser() when given invalid credentials, throws BadCredentialsException and returns Unauthorized status")
  void loginUser_WithInvalidCredentials_ThrowsException() throws Exception {
    LoginRequest validLoginRequest = new LoginRequest("test@example.com", "password123");
    when(authService.loginUser(validLoginRequest.email(), validLoginRequest.password()))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(validLoginRequest)))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotHaveJsonPath())
        .andExpect(jsonPath("$.message", CoreMatchers.isA(String.class)));
  }

  @ParameterizedTest
  @MethodSource("invalidLoginRequestProvider")
  @DisplayName("Test loginUser() when given invalid input, returns Bad Request status")
  void loginUser_WithInvalidInput_ReturnsBadRequest(String email, String password)
      throws Exception {
    LoginRequest invalidRequest = new LoginRequest(email, password);

    mockMvc.perform(post("/api/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.data").doesNotHaveJsonPath())
        .andExpect(jsonPath("$.message", CoreMatchers.isA(String.class)));

    verifyNoInteractions(authService);
  }
}
