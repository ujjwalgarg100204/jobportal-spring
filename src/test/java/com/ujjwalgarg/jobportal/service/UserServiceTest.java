package com.ujjwalgarg.jobportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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

  private User testUser;

  @BeforeEach
  void setUp() {
    Role role = new Role();
    role.setName(ERole.ROLE_CANDIDATE);

    testUser = User.builder()
        .email("test@example.com")
        .password("password")
        .role(role)
        .build();
  }


  @Test
  @DisplayName("Test createNewCandidate() when user doesn't exist")
  void createNewCandidate_UserDoesNotExist_ReturnsUser() {
    CandidateProfile mockCProfile = new CandidateProfile();
    when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
    when(roleService.getRoleByName(ERole.ROLE_CANDIDATE)).thenReturn(testUser.getRole());
    when(userRepository.save(any(User.class))).thenReturn(testUser);
    when(cProfileRepository.save(any(CandidateProfile.class))).thenReturn(mockCProfile);

    User result = userService.createNewCandidate(testUser, mockCProfile);

    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getRole()).isEqualTo(testUser.getRole());
  }

  @Test
  @DisplayName("Test createNewCandidate() when user already exists")
  void createNewCandidate_UserAlreadyExists_ThrowsAlreadyPresentException() {
    CandidateProfile mockCProfile = new CandidateProfile();
    when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

    assertThrows(AlreadyPresentException.class,
        () -> userService.createNewCandidate(testUser, mockCProfile));
  }

  @Test
  @DisplayName("Test createNewRecruiter() when user doesn't exist")
  void createNewRecruiter_UserDoesNotExist_ReturnsUser() {
    RecruiterProfile mockRProfile = new RecruiterProfile();
    testUser.getRole().setName(ERole.ROLE_RECRUITER);
    when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
    when(roleService.getRoleByName(ERole.ROLE_RECRUITER)).thenReturn(testUser.getRole());
    when(userRepository.save(any(User.class))).thenReturn(testUser);
    when(rProfileRepository.save(any(RecruiterProfile.class))).thenReturn(mockRProfile);

    User result = userService.createNewRecruiter(testUser, mockRProfile);

    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getRole()).isEqualTo(testUser.getRole());
  }

  @Test
  @DisplayName("Test createNewRecruiter() when user already exists")
  void createNewRecruiter_UserAlreadyExists_ThrowsAlreadyPresentException() {
    RecruiterProfile mockRProfile = new RecruiterProfile();
    when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

    assertThrows(AlreadyPresentException.class,
        () -> userService.createNewRecruiter(testUser, mockRProfile));
  }

  @Test
  @DisplayName("Test getUserByEmail() when user exists")
  void getUserByEmail_UserExists_ReturnsUser() {
    String email = "test@example.com";
    testUser.setEmail(email);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(testUser));

    User result = userService.getUserByEmail(email);

    Assertions.assertThat(result).isNotNull();
    Assertions.assertThat(result.getEmail()).isEqualTo(email);
  }

  @Test
  @DisplayName("Test getUserByEmail() when user does not exist")
  void getUserByEmail_UserDoesNotExist_ThrowsNotFoundException() {
    String email = "test@example.com";
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.getUserByEmail(email));
  }


  @Test
  @DisplayName("Test loadUserByUsername() when user exists")
  void loadUserByUsername_UserExists_ReturnsUserDetails() {
    when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

    UserDetails userDetails = userService.loadUserByUsername("test@example.com");

    assertNotNull(userDetails);
    assertEquals("test@example.com", userDetails.getUsername());
    assertTrue(userDetails.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE")));

    verify(userRepository, times(1)).findByEmail("test@example.com");
  }

  @Test
  @DisplayName("Test loadUserByUsername() when user does not exist")
  void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
    when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

    assertThrows(UsernameNotFoundException.class,
        () -> userService.loadUserByUsername("nonexistent@example.com"));

    verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
  }

  @Test
  @DisplayName("Test loadUserByUsername() when email is null")
  void loadUserByUsername_EmailIsNull_ThrowsUsernameNotFoundException() {
    assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(null));

    verify(userRepository, times(1)).findByEmail(null);
  }

}
