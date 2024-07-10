package com.ujjwalgarg.jobportal.configuration;

import com.ujjwalgarg.jobportal.constant.ERole;
import com.ujjwalgarg.jobportal.entity.Role;
import com.ujjwalgarg.jobportal.service.RoleService;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

  private final RoleService roleService;

  @Transactional
  @Override
  public void run(String... args) {
    Arrays.stream(ERole.values())
        .filter(role -> !roleService.checkIfExistsByName(role))
        .forEach(role -> roleService.createNew(Role.builder().name(role).build()));
  }

}
