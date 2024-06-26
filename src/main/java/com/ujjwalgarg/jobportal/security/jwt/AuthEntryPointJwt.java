package com.ujjwalgarg.jobportal.security.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security component for handling unauthorized access attempts.
 * This class implements AuthenticationEntryPoint and defines how the
 * server responds when a user tries to access a protected resource
 * without proper authentication.
 */
@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    /**
     * Spring Security entry point for unauthorized access attempts.
     * 
     * This method is invoked when a user tries to access a protected
     * resource without proper authentication. It logs the error message
     * and sends a JSON response to the client indicating unauthorized
     * access with details like error message and requested path.
     *
     * @param request       The HttpServletRequest object representing the incoming
     *                      request.
     * @param response      The HttpServletResponse object for sending the response
     *                      to the client.
     * @param authException The AuthenticationException object containing details
     *                      about the authentication failure.
     * @throws IOException      If an I/O error occurs while writing the response.
     * @throws ServletException If a Servlet error occurs.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
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
