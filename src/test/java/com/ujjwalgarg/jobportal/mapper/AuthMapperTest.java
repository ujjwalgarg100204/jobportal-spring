package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.controller.payload.common.RoleDto;
import com.ujjwalgarg.jobportal.controller.payload.common.UserDto;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.entity.User;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthMapperTest {

  @Autowired
  private AuthMapper authMapper;

  private static Stream<Arguments> provideUsersForMapping() {
    LocalDate now = LocalDate.now();
    return Stream.of(
        Arguments.of(
            createUser(1, "user1@example.com", "password1", now, ERole.ROLE_CANDIDATE),
            createUserDto(1, "user1@example.com", now, "ROLE_CANDIDATE")
        ),
        Arguments.of(
            createUser(2, "admin@example.com", "adminpass", now, ERole.ROLE_RECRUITER),
            createUserDto(2, "admin@example.com", now, "ROLE_RECRUITER")
        ),
        Arguments.of(
            createUser(3, "mod@example.com", "modpass", now, ERole.ROLE_CANDIDATE),
            createUserDto(3, "mod@example.com", now, "ROLE_CANDIDATE")
        )
    );
  }

  private static User createUser(Integer id, String email, String password,
      LocalDate registrationDate, ERole roleName) {
    User user = new User();
    user.setId(id);
    user.setEmail(email);
    user.setPassword(password);
    user.setRegistrationDate(registrationDate);
    Role role = new Role();
    role.setName(roleName);
    user.setRole(role);
    return user;
  }

  private static UserDto createUserDto(Integer id, String email,
      LocalDate registrationDate, String roleName) {
    UserDto userDto = new UserDto();
    userDto.setId(id);
    userDto.setEmail(email);
    userDto.setRegistrationDate(registrationDate);
    RoleDto roleDto = new RoleDto();
    roleDto.setName(roleName);
    userDto.setRole(roleDto);
    return userDto;
  }

  @ParameterizedTest
  @MethodSource("provideUsersForMapping")
  void fromUserToUserDto_ShouldMapCorrectly(User input, UserDto expected) {
    UserDto result = authMapper.fromUserToUserDto(input);

    assertThat(result).isNotNull();
    assertThat(result.getId()).isEqualTo(expected.getId());
    assertThat(result.getEmail()).isEqualTo(expected.getEmail());
    assertThat(result.getRegistrationDate()).isEqualTo(expected.getRegistrationDate());

    if (expected.getRole() != null) {
      assertThat(result.getRole()).isNotNull();
      assertThat(result.getRole().getName()).isEqualTo(expected.getRole().getName());
    } else {
      assertThat(result.getRole()).isNull();
    }
  }

}
