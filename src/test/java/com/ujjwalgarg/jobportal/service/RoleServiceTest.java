package com.ujjwalgarg.jobportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.exception.AlreadyPresentException;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.repository.RoleRepository;
import com.ujjwalgarg.jobportal.service.impl.RoleServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
  private RoleServiceImpl roleService;

  private Role mockRole;

  @BeforeEach
  void setUp() {
    mockRole = Role.builder().name(ERole.ROLE_CANDIDATE).build();
  }

  @Test
  @DisplayName("Test getRoleByName() when role exists")
  void getRoleByName_RoleExists_ReturnsRole() {
    when(roleRepository.findByName(ERole.ROLE_CANDIDATE)).thenReturn(Optional.of(mockRole));
    Role foundRole = roleService.getRoleByName(ERole.ROLE_CANDIDATE);
    assertEquals(mockRole, foundRole);
  }

  @Test
  @DisplayName("Test getRoleByName() when role does not exist")
  void getRoleByName_RoleDoesNotExist_ThrowsNotFoundException() {
    when(roleRepository.findByName(ERole.ROLE_CANDIDATE)).thenReturn(Optional.empty());
    assertThrows(NotFoundException.class, () -> roleService.getRoleByName(ERole.ROLE_CANDIDATE));
  }

  @Test
  @DisplayName("Test checkIfExistsByName() when role exists")
  void checkIfExistsByName_RoleExists_ReturnsTrue() {
    when(roleRepository.existsByName(ERole.ROLE_CANDIDATE)).thenReturn(true);
    assertTrue(roleService.checkIfExistsByName(ERole.ROLE_CANDIDATE));
  }

  @Test
  @DisplayName("Test checkIfExistsByName() when role does not exist")
  void checkIfExistsByName_RoleDoesNotExist_ReturnsFalse() {
    when(roleRepository.existsByName(ERole.ROLE_CANDIDATE)).thenReturn(false);
    assertFalse(roleService.checkIfExistsByName(ERole.ROLE_CANDIDATE));
  }

  @Test
  @DisplayName("Test createNew() when role does not exist")
  void createNew_RoleDoesNotExist_ReturnsRole() {
    when(roleRepository.existsByName(any())).thenReturn(false);
    when(roleRepository.save(any(Role.class))).thenReturn(mockRole);

    Role createdRole = roleService.createNew(mockRole);

    assertNotNull(createdRole);
    assertEquals(ERole.ROLE_CANDIDATE, createdRole.getName());
  }

  @Test
  @DisplayName("Test createNew() when role already exists")
  void createNew_RoleAlreadyExists_ThrowsAlreadyPresentException() {
    when(roleRepository.existsByName(any())).thenReturn(true);
    assertThrows(AlreadyPresentException.class, () -> roleService.createNew(mockRole));
  }
}
