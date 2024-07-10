package com.ujjwalgarg.jobportal.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
import com.ujjwalgarg.jobportal.service.impl.UserServiceImpl;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private RoleService roleService;
  @Mock
  private CandidateProfileRepository cProfileRepository;
  @Mock
  private RecruiterProfileRepository rProfileRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @Test
  void createNewCandidate_ReturnsUser() {
    User mockUser = User.builder().email("test@gmail.com").build();
    CandidateProfile mockCProfile = new CandidateProfile();
    Role mockCandidateRole = new Role();
    when(userRepository.existsByEmail(mockUser.getEmail())).thenReturn(false);
    when(roleService.getRoleByName(ERole.ROLE_CANDIDATE)).thenReturn(mockCandidateRole);
    when(userRepository.save(any(User.class))).thenReturn(mockUser);
    when(cProfileRepository.save(any(CandidateProfile.class))).thenReturn(mockCProfile);

    User result = userService.createNewCandidate(mockUser, mockCProfile);

    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getRole()).isEqualTo(mockCandidateRole);
  }

  @Test
  void createNewCandidate_ThrowsAlreadyPresentException() {
    User mockUser = User.builder().email("test@gmail.com").build();
    CandidateProfile mockCProfile = new CandidateProfile();
    when(userRepository.existsByEmail(mockUser.getEmail())).thenReturn(true);

    assertThrows(AlreadyPresentException.class,
        () -> userService.createNewCandidate(mockUser, mockCProfile));
  }

  @Test
  void createNewRecruiter_ReturnsUser() {
    User mockUser = User.builder().email("recruiter@example.com").build();
    RecruiterProfile mockRProfile = new RecruiterProfile();
    Role mockRecruiterRole = new Role();
    when(userRepository.existsByEmail(mockUser.getEmail())).thenReturn(false);
    when(roleService.getRoleByName(ERole.ROLE_RECRUITER)).thenReturn(mockRecruiterRole);
    when(userRepository.save(any(User.class))).thenReturn(mockUser);
    when(rProfileRepository.save(any(RecruiterProfile.class))).thenReturn(mockRProfile);

    User result = userService.createNewRecruiter(mockUser, mockRProfile);

    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getRole()).isEqualTo(mockRecruiterRole);
  }

  @Test
  void createNewRecruiter_ThrowsAlreadyPresentException() {
    User mockUser = User.builder().email("recruiter@example.com").build();
    RecruiterProfile mockRProfile = new RecruiterProfile();
    when(userRepository.existsByEmail(mockUser.getEmail())).thenReturn(true);

    assertThrows(AlreadyPresentException.class,
        () -> userService.createNewRecruiter(mockUser, mockRProfile));
  }

  @Test
  void getUserByEmail_ReturnsUser() {
    String email = "test@example.com";
    User mockUser = User.builder().email(email).build();
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));

    User result = userService.getUserByEmail(email);

    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getEmail()).isEqualTo(email);
  }

  @Test
  void getUserByEmail_ThrowsNotFoundException() {
    String email = "test@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.getUserByEmail(email));
  }
}
