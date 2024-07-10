package com.ujjwalgarg.jobportal.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ujjwalgarg.jobportal.controller.payload.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    log.error("Unauthorized access to resource:{}, failed with error:{}", request.getServletPath(),
        authException.toString());

    var res = Response.failure(
        String.format("You are not authorized to access this resource:%s, error message:%s",
            request.getServletPath(), authException.getMessage()));
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    new ObjectMapper().writeValue(response.getOutputStream(), res);
  }
}
