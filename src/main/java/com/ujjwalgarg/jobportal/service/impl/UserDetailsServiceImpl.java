package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "USER_DETAILS_SERVICE")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(@Valid @NotNull @Email String email)
      throws UsernameNotFoundException {
    User foundUser =
        this.userRepository
            .findByEmail(email)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "No users exists with email %s".formatted(email)));
    List<SimpleGrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority(foundUser.getRole().getName().toString()));

    return new UserDetails() {
      @Override
      public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
      }

      @Override
      public String getPassword() {
        return foundUser.getPassword();
      }

      @Override
      public String getUsername() {
        return foundUser.getEmail();
      }
    };
  }
}
