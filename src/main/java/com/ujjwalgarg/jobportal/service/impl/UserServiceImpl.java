package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.CandidateProfile;
import com.ujjwalgarg.jobportal.entity.Company;
import com.ujjwalgarg.jobportal.entity.RecruiterProfile;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.exception.AlreadyPresentException;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.repository.CandidateProfileRepository;
import com.ujjwalgarg.jobportal.repository.RecruiterProfileRepository;
import com.ujjwalgarg.jobportal.repository.UserRepository;
import com.ujjwalgarg.jobportal.service.CompanyService;
import com.ujjwalgarg.jobportal.service.RoleService;
import com.ujjwalgarg.jobportal.service.UserService;
import com.ujjwalgarg.jobportal.validator.Create;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

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
  private final PasswordEncoder passwordEncoder;
  private final CompanyService companyService;

  /**
   * Creates a new candidate user along with their profile.
   *
   * @param user     The user details to be saved.
   * @param cProfile The candidate profile details to be associated with the user.
   * @throws AlreadyPresentException if a user with the same email already exists.
   * @throws NotFoundException       if the candidate role is not found in the system.
   */
  @Transactional
  public void createNewCandidate(@Validated(Create.class) User user,
      @Validated(Create.class) CandidateProfile cProfile)
      throws AlreadyPresentException {
    // check if candidate already exists
    if (this.userRepository.existsByEmail(user.getEmail())) {
      log.warn("Candidate with email:{} already exists", user.getEmail());
      throw new AlreadyPresentException(
          "Candidate with email:" + user.getEmail() + " already exists");
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));

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
  }

  @Transactional
  public void createNewRecruiter(@Validated(Create.class) User user,
      @Validated(Create.class) RecruiterProfile rProfile)
      throws AlreadyPresentException {
    // check if user already exists
    if (this.userRepository.existsByEmail(user.getEmail())) {
      log.warn("Recruiter with email:{} already exists", user.getEmail());
      throw new AlreadyPresentException(
          "Recruiter with email:" + user.getEmail() + " already exists");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role role = this.roleService.getRoleByName(ERole.ROLE_RECRUITER);
    user.setRole(role);

    User savedUser = this.userRepository.save(user);
    log.info("New recruiter created with id:{}", savedUser.getId());

    if (rProfile.getCompany().getId() != null) {
      Company company = companyService.getCompanyById(rProfile.getCompany().getId());
      rProfile.setCompany(company);
    }
    rProfile.setUser(savedUser);
    RecruiterProfile savedRProfile = rProfileRepository.save(rProfile);
    log.info(
        "Recruiter profile with id:{} created for user with id:{}",
        savedRProfile.getId(),
        savedUser.getId());
  }

  public User getUserByEmail(@Valid @NotNull @Email String email) throws NotFoundException {
    return this.userRepository.findByEmail(email)
        .orElseThrow(() -> {
          log.error("User with email:{} not found", email);
          return new NotFoundException("User with email:" + email + " not found");
        });
  }

  @Override
  public User getUserById(int id) throws NotFoundException {
    return this.userRepository.findById(id).orElseThrow(() -> {
      log.error("User with id:{} not found", id);
      return new NotFoundException("User with id:" + id + " not found");
    });
  }
}
