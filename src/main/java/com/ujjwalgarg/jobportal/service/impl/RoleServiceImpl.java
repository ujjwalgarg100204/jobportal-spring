package com.ujjwalgarg.jobportal.service.impl;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.exception.AlreadyPresentException;
import com.ujjwalgarg.jobportal.exception.NotFoundException;
import com.ujjwalgarg.jobportal.repository.RoleRepository;
import com.ujjwalgarg.jobportal.service.RoleService;
import com.ujjwalgarg.jobportal.validator.Create;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j(topic = "ROLE_SERVICE")
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  public Role getRoleByName(@Valid @NotNull ERole name) {
    return this.roleRepository.findByName(name).orElseThrow(() -> {
      log.error("Role {} not found", name);
      return new NotFoundException("Role " + name + " not found");
    });
  }

  public boolean checkIfExistsByName(@NotNull ERole name) {
    return this.roleRepository.existsByName(name);
  }

  public Role createNew(@Validated(Create.class) Role role) {
    // check if role already exists
    if (this.roleRepository.existsByName(role.getName())) {
      log.warn("Role {} already exists", role.getName());
      throw new AlreadyPresentException("Role " + role.getName() + " already exists");
    }
    return this.roleRepository.save(role);
  }
}
