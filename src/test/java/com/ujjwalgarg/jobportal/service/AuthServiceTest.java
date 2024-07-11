package com.ujjwalgarg.jobportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ujjwalgarg.jobportal.annotation.WithMockCandidate;
import com.ujjwalgarg.jobportal.config.SecurityContextServiceTest;
import com.ujjwalgarg.jobportal.entity.User;
import com.ujjwalgarg.jobportal.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AuthServiceTest extends SecurityContextServiceTest {

  @Autowired
  private AuthServiceImpl authService;

  @Test
  @DisplayName("Test getAuthenticatedUser() when user is authenticated")
  @WithMockCandidate
  void getAuthenticatedUser_UserIsAuthenticated_ReturnsUser() {
    User expectedUser = User.builder().email(CANDIDATE_EMAIL).build();
    when(userService.getUserByEmail(CANDIDATE_EMAIL)).thenReturn(expectedUser);

    User result = authService.getAuthenticatedUser();

    assertNotNull(result);
    assertEquals(expectedUser.getEmail(), result.getEmail());
  }

}
