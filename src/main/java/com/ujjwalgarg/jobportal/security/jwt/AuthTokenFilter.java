package com.ujjwalgarg.jobportal.security.jwt;

import com.ujjwalgarg.jobportal.security.services.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Spring Security filter for validating JWT tokens in requests. This filter is executed only once
 * per request and checks for a JWT token in the Authorization header. If a valid token is found, it
 * extracts user information and sets the user authentication context in Spring Security.
 */
@Slf4j
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final UserDetailsServiceImpl userDetailsService;

  @Autowired
  public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
  }

  /**
   * This method intercepts incoming requests and attempts to verify the JWT token in the
   * Authorization header. If a valid token is found, it sets the user authentication in the
   * SecurityContext based on the extracted user information.
   *
   * @param request     The HttpServletRequest object representing the incoming request.
   * @param response    The HttpServletResponse object for sending responses.
   * @param filterChain The chain of filters to be invoked after this filter.
   * @throws ServletException If a Servlet error occurs.
   * @throws IOException      If an I/O error occurs.
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String email = jwtUtils.getEmailFromJwtToken(jwt);
        log.info("Email: {}", email);

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      log.error("Cannot set user authentication: {}", e.toString());
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Extracts the JWT token from the Authorization header of the request. It checks for the
   * "Authorization" header and verifies if it starts with "Bearer ". If valid, it extracts the
   * token string after "Bearer ".
   *
   * @param request The HttpServletRequest object representing the incoming request.
   * @return The extracted JWT token string or null if not found or invalid format.
   */
  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }
}
