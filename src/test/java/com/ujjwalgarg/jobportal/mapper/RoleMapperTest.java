package com.ujjwalgarg.jobportal.mapper;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.controller.payload.common.RoleDto;
import com.ujjwalgarg.jobportal.entity.Role;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RoleMapperTest {

  @Autowired
  private RoleMapper roleMapper;

  private static Stream<Arguments> provideRolesForMapping() {
    return Stream.of(
        Arguments.of(
            createRole(1, ERole.ROLE_RECRUITER),
            createRoleDto("ROLE_RECRUITER")
        ),
        Arguments.of(
            createRole(2, ERole.ROLE_CANDIDATE),
            createRoleDto("ROLE_CANDIDATE")
        )
    );
  }

  private static Role createRole(Integer id, ERole name) {
    Role role = new Role();
    role.setId(id);
    role.setName(name);
    return role;
  }

  private static RoleDto createRoleDto(String name) {
    RoleDto roleDto = new RoleDto();
    roleDto.setName(name);
    return roleDto;
  }

  @ParameterizedTest
  @MethodSource("provideRolesForMapping")
  void fromRoleToRoleDto_ShouldMapCorrectly(Role input, RoleDto expected) {
    RoleDto result = roleMapper.fromRoleToRoleDto(input);

    assertThat(result).isNotNull();
    assertThat(result.getName()).isEqualTo(expected.getName());
  }
}
