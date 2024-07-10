package com.ujjwalgarg.jobportal.security;

import com.ujjwalgarg.jobportal.security.jwt.AuthEntryPointJwt;
import com.ujjwalgarg.jobportal.security.jwt.AuthTokenFilter;
import com.ujjwalgarg.jobportal.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for setting up web security in the application. This class configures
 * authentication and authorization using Spring Security.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

  private final UserDetailsServiceImpl userDetailsService;
  private final AuthEntryPointJwt unauthorizedHandler;
  private final AuthTokenFilter authTokenFilter;

  private final String[] publicUrls = {
      "/api/auth/**",
  };

  @Autowired
  public WebSecurityConfig(
      UserDetailsServiceImpl userDetailsService,
      AuthEntryPointJwt unauthorizedHandler,
      AuthTokenFilter authTokenFilter) {
    this.userDetailsService = userDetailsService;
    this.unauthorizedHandler = unauthorizedHandler;
    this.authTokenFilter = authTokenFilter;
  }

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return authTokenFilter;
  }

  /**
   * Bean for the DAO authentication provider. This provider is responsible for authenticating users
   * with a username and password.
   *
   * @return the {@link DaoAuthenticationProvider} bean
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());

    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
      throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Bean definition for the SecurityFilterChain. This method configures various security aspects
   * like CSRF, exception handling, session management, authorization rules, authentication
   * providers, and filter chain.
   *
   * @param http The HttpSecurity object used for security configuration.
   * @return The configured SecurityFilterChain.
   * @throws Exception If an error occurs during security configuration.
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth -> auth.requestMatchers(publicUrls).permitAll().anyRequest().authenticated());

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(
        authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
