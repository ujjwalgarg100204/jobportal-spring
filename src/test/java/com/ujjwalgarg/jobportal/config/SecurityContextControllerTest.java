package com.ujjwalgarg.jobportal.config;

import com.ujjwalgarg.jobportal.configuration.security.AuthEntryPointJwt;
import com.ujjwalgarg.jobportal.configuration.security.WebSecurityConfig;
import com.ujjwalgarg.jobportal.service.UserService;
import com.ujjwalgarg.jobportal.util.JwtUtils;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@Import({WebSecurityConfig.class, AuthEntryPointJwt.class})
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
public abstract class SecurityContextControllerTest {

  @MockBean
  protected UserService userService;

  @MockBean
  protected JwtUtils jwtUtils;

}
