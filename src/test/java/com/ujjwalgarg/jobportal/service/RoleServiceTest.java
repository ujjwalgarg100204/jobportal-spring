package com.ujjwalgarg.jobportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.exception.AlreadyPresentException;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.repository.RoleRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

  @Mock
  private RoleRepository roleRepository;

  @InjectMocks
  private RoleService roleService;

  private Role mockRole;

  @BeforeEach
  void setUp() {
    mockRole = Role.builder().name(ERole.ROLE_CANDIDATE).build();
  }

  @Test
  void getRoleByName_whenRoleExists_thenReturnsRole() {
    when(roleRepository.findByName(ERole.ROLE_CANDIDATE)).thenReturn(Optional.of(mockRole));
    Role foundRole = roleService.getRoleByName(ERole.ROLE_CANDIDATE);
    assertEquals(mockRole, foundRole);
  }

  @Test
  void getRoleByName_whenRoleDoesNotExist_thenThrowNotFoundException() {
    when(roleRepository.findByName(ERole.ROLE_CANDIDATE)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> roleService.getRoleByName(ERole.ROLE_CANDIDATE));
  }

  @Test
  void checkIfExistsByName_whenRoleExists_thenReturnsTrue() {
    when(roleRepository.existsByName(ERole.ROLE_CANDIDATE)).thenReturn(true);
    assertTrue(roleService.checkIfExistsByName(ERole.ROLE_CANDIDATE));
  }

  @Test
  void checkIfExistsByName_whenRoleDoesNotExist_thenReturnsFalse() {
    when(roleRepository.existsByName(ERole.ROLE_CANDIDATE)).thenReturn(false);
    assertFalse(roleService.checkIfExistsByName(ERole.ROLE_CANDIDATE));
  }

  @Test
  void createNew_whenRoleDoesNotExist_thenReturnsRole() {
    when(roleRepository.existsByName(any())).thenReturn(false);
    when(roleRepository.save(any(Role.class))).thenReturn(mockRole);

    Role createdRole = roleService.createNew(mockRole);

    assertNotNull(createdRole);
    assertEquals(ERole.ROLE_CANDIDATE, createdRole.getName());
  }

  @Test
  void createNew_whenRoleAlreadyExists_thenThrowAlreadyPresentException() {
    when(roleRepository.existsByName(any())).thenReturn(true);
    assertThrows(AlreadyPresentException.class, () -> roleService.createNew(mockRole));
  }
}
