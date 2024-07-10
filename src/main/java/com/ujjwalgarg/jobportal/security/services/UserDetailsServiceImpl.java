package com.ujjwalgarg.jobportal.security.services;

import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} interface. This service is responsible for loading
 * user-specific data.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Autowired
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Loads the user's details by their email. This method is called by Spring Security during the
   * authentication process.
   *
   * @param email the email of the user whose data is required
   * @return {@link UserDetails} object containing user details
   * @throws UsernameNotFoundException if no user found with the given email
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "No users exists with email %s".formatted(email)));

    return UserDetailsImpl.buildFromUser(user);
  }
}
