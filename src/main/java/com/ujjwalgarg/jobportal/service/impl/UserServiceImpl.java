package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.AlreadyPresentException;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.repository.CandidateProfileRepository;
import com.ujjwalgarg.jobportal.repository.RecruiterProfileRepository;
import com.ujjwalgarg.jobportal.repository.UserRepository;
import com.ujjwalgarg.jobportal.service.RoleService;
import com.ujjwalgarg.jobportal.service.UserService;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserService
 */
@Service
@RequiredArgsConstructor
@Slf4j(topic = "USER_SERVICE")
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleService roleService;
  private final CandidateProfileRepository cProfileRepository;
  private final RecruiterProfileRepository rProfileRepository;

  /**
   * Creates a new candidate user along with their profile.
   *
   * @param user     The user details to be saved.
   * @param cProfile The candidate profile details to be associated with the user.
   * @return The saved user entity with a candidate role.
   * @throws AlreadyPresentException if a user with the same email already exists.
   * @throws NotFoundException       if the candidate role is not found in the system.
   */
  @Transactional
  public User createNewCandidate(User user, CandidateProfile cProfile)
      throws AlreadyPresentException {
    // check if candidate already exists
    if (this.userRepository.existsByEmail(user.getEmail())) {
      log.warn("Candidate with email:{} already exists", user.getEmail());
      throw new AlreadyPresentException(
          "Candidate with email:" + user.getEmail() + " already exists");
    }

    Role candidateRole = this.roleService.getRoleByName(ERole.ROLE_CANDIDATE);
    user.setRole(candidateRole);

    User savedUser = this.userRepository.save(user);
    log.info("New candidate created with id:{}", savedUser.getId());

    cProfile.setUser(savedUser);
    CandidateProfile savedCProfile = cProfileRepository.save(cProfile);
    log.info(
        "Candidate profile with id:{} created for user with id:{}",
        savedCProfile.getId(),
        savedUser.getId());

    return savedUser;
  }

  @Transactional
  public User createNewRecruiter(User user, RecruiterProfile rProfile)
      throws AlreadyPresentException {
    // check if user already exists
    if (this.userRepository.existsByEmail(user.getEmail())) {
      log.warn("Recruiter with email:{} already exists", user.getEmail());
      throw new AlreadyPresentException(
          "Recruiter with email:" + user.getEmail() + " already exists");
    }

    Role role = this.roleService.getRoleByName(ERole.ROLE_RECRUITER);

    user.setRole(role);
    User savedUser = this.userRepository.save(user);
    log.info("New recruiter created with id:{}", savedUser.getId());

    rProfile.setUser(savedUser);
    RecruiterProfile savedRProfile = rProfileRepository.save(rProfile);
    log.info(
        "Recruiter profile with id:{} created for user with id:{}",
        savedRProfile.getId(),
        savedUser.getId());

    return savedUser;
  }

  public User getUserByEmail(String email) throws NotFoundException {
    return this.userRepository.findByEmail(email)
        .orElseThrow(() -> {
          log.error("User with email:{} not found", email);
          return new NotFoundException("User with email:" + email + " not found");
        });
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
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
