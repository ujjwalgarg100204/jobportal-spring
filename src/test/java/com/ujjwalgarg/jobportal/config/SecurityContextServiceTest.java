package com.ujjwalgarg.jobportal.config;

import com.ujjwalgarg.jobportal.service.UserService;
import com.ujjwalgarg.jobportal.util.JwtUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ContextConfiguration
public abstract class SecurityContextServiceTest {

  protected static final String CANDIDATE_EMAIL = "candidate@gmail.com";
  protected static final String RECRUITER_EMAIL = "recruiter@gmail.com";
  protected static final String PASSWORD = "password";
  @MockBean
  protected UserService userService;
  @MockBean
  protected JwtUtils jwtUtils;

}
