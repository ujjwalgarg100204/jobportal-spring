package com.ujjwalgarg.jobportal.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Spring Security component for handling unauthorized access attempts. This class implements
 * AuthenticationEntryPoint and defines how the server responds when a user tries to access a
 * protected resource without proper authentication.
 */
@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

  /**
   * Spring Security entry point for unauthorized access attempts.
   *
   * <p>This method is invoked when a user tries to access a protected resource without proper
   * authentication. It logs the error message and sends a JSON response to the client indicating
   * unauthorized access with details like error message and requested path.
   *
   * @param request       The HttpServletRequest object representing the incoming request.
   * @param response      The HttpServletResponse object for sending the response to the client.
   * @param authException The AuthenticationException object containing details about the
   *                      authentication failure.
   * @throws IOException If an I/O error occurs while writing the response.
   */
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    log.error("Unauthorized error: {}", authException.getMessage());

    Map<String, Object> body = new HashMap<>();
    body.put("success", false);
    body.put("error", "Unauthorized");
    body.put("message", authException.getMessage());
    body.put("path", request.getServletPath());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("Content-Type", "application/json");
    ObjectMapper mapper = new ObjectMapper();
    mapper.writeValue(response.getOutputStream(), body);
  }
}
